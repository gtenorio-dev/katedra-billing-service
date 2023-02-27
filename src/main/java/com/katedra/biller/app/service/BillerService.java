package com.katedra.biller.app.service;

import com.katedra.biller.app.client.gen.*;
import com.katedra.biller.app.dto.BillDetailDTO;
import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.entity.AccountEntity;
import com.katedra.biller.app.model.TicketAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BillerService {

    private static final Logger logger = LoggerFactory.getLogger(BillerService.class);

    private static final String APROBADO = "A";
    private static final String RECHAZADO = "R";
    private static final String PARCIAL = "P";

//    @Autowired
//    @Qualifier("authentications")
//    Map<Long, TicketAccess> authentications;

    @Autowired
    private AfipWSAAService wsaaService;
    @Autowired
    private AfipWSFEService wsfeService;
    @Autowired
    private AccountService accountService;
    @Value("${afip.billing.concepto}")
    private int concepto;
    @Value("${afip.billing.cbteTipo}")
    private int cbteTipo;
    @Value("${afip.billing.docTipo}")
    private int docTipo;
    @Value("${afip.billing.impTotConc}")
    private int impTotConc;
    @Value("${afip.billing.impOpEx}")
    private int impOpEx;
    @Value("${afip.billing.impIVA}")
    private int impIVA;
    @Value("${afip.billing.monId}")
    private String monId;
    @Value("${afip.billing.monCotiz}")
    private int monCotiz;


    public FECAESolicitarResponse create(BillingPayload billingPayload) throws Exception {

        AccountEntity account = accountService.findByCuit(billingPayload.getCuit());

//        FECAESolicitarResponse fecaeSolicitarResponse = wsfeService.generateBill(
//                getFEAuthRequest(billingPayload.getCuit()), buildBillRequest(billingPayload, account));
//
//        // TODO Split response
//
//        validateBill(fecaeSolicitarResponse.getFECAESolicitarResult());


        return wsfeService.generateBill(getFEAuthRequest(account), buildBillRequest(billingPayload, account));
    }

    private boolean validateBill(FECAEResponse res) {
        String errorMessage = "";

        switch (res.getFeCabResp().getResultado()) {
            case APROBADO:
                logger.info("APROBADO");
                break;
            case RECHAZADO:
                logger.info("RECHAZADO");
                errorMessage = buildErrorMessage(res.getErrors().getErr());
                break;
            case PARCIAL:
                logger.info("PARCIAL");
                break;
        }


        return false;
    }

    private String buildErrorMessage(List<Err> errors) {
        StringBuilder errorMessages = new StringBuilder();
        errors.forEach(err -> errorMessages.append(String.valueOf(err.getCode()).concat(" - ").concat(err.getMsg())));
        return errorMessages.toString();
    }

    public FECompUltimoAutorizadoResponse getUltimoComprobanteAutorizado(Long cuit) throws Exception {
        AccountEntity account = accountService.findByCuit(cuit);
        return wsfeService.getUltimoComprobanteAutorizado(
                getFEAuthRequest(account), account.getPuntoVenta(), cbteTipo);
    }


    /**
     * Devuelve una Autenticacion valida para usar el servicio de Facturacion Electronica
     *
     * @param account
     * @return FEAuthRequest
     * @throws Exception
     */
    private FEAuthRequest getFEAuthRequest(AccountEntity account) throws Exception {
        if (account.getExpirationTime() == null) {
            TicketAccess ta = wsaaService.authenticate(new TicketAccess());
            account = accountService.updateSession(account, ta);
        } else {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
			Date expirationTime = dateFormat.parse(account.getExpirationTime());
            Date currentDateTime = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expirationTime);
            calendar.add(Calendar.MINUTE, -10);
            Date expirationTimeLimit = calendar.getTime();

            if (expirationTime.compareTo(currentDateTime) < 0) {
                logger.info("Sesion vencida");
                TicketAccess ta = wsaaService.authenticate(new TicketAccess());
                account = accountService.updateSession(account, ta);
            } else if (expirationTimeLimit.compareTo(currentDateTime) < 0) {
                logger.info("Sesion vence en menos de 10 minutos. Se renueva");
                TicketAccess ta = wsaaService.authenticate(new TicketAccess());
                account = accountService.updateSession(account, ta);
            }

            //TODO Exception --> No se puede obtener el Ticket de Acceso
        }

        FEAuthRequest authRequest = new FEAuthRequest();
        authRequest.setCuit(account.getCuit());
        authRequest.setToken(account.getToken());
        authRequest.setSign(account.getSign());

        return authRequest;
    }

    private FECAERequest buildBillRequest(BillingPayload billingPayload, AccountEntity account) throws Exception {
        FECAERequest fecaeRequest = new FECAERequest();

        ArrayOfFECAEDetRequest arrayOfFECAEDetRequest = new ArrayOfFECAEDetRequest();
        List<FECAEDetRequest> fecaeDetRequests = arrayOfFECAEDetRequest.getFECAEDetRequest();

        int ultimoComprobanteAutorizado = wsfeService
                .getUltimoComprobanteAutorizado(
                        getFEAuthRequest(account), account.getPuntoVenta(), cbteTipo)
                .getFECompUltimoAutorizadoResult().getCbteNro();

        for (BillDetailDTO detail : billingPayload.getDetails()) {
            FECAEDetRequest detRequest = buildDetails(detail, account.getPuntoVenta(), ++ultimoComprobanteAutorizado);
            fecaeDetRequests.add(detRequest);
        }

        fecaeRequest.setFeDetReq(arrayOfFECAEDetRequest);
        fecaeRequest.setFeCabReq(buildHeader(account.getPuntoVenta(), fecaeDetRequests.size()));
        return fecaeRequest;
    }

    private FECAECabRequest buildHeader(int ptoVenta, int cantReg) {
        FECAECabRequest fecaeCabRequest = new FECAECabRequest();
        fecaeCabRequest.setPtoVta(ptoVenta); // Punto de venta 2
        fecaeCabRequest.setCbteTipo(cbteTipo); // Factura C - Monotributo
        fecaeCabRequest.setCantReg(cantReg); // Cantidad de productos a facturar
        return fecaeCabRequest;
    }

    private FECAEDetRequest buildDetails(BillDetailDTO detail, int ptoVenta, int numComprobante) throws Exception {
        // Generate Details
        FECAEDetRequest feCAEDetRequest = new FECAEDetRequest();
        feCAEDetRequest.setConcepto(concepto); // Productos y servicios = 3
        feCAEDetRequest.setDocTipo(docTipo); // Tipo DNI = 96
        feCAEDetRequest.setDocNro(detail.getDniComprador()); // Numero de DNI del comprador

        feCAEDetRequest.setCbteDesde(numComprobante);
        feCAEDetRequest.setCbteHasta(numComprobante);
        feCAEDetRequest.setCbteFch("20230217"); // Formato yyyymmdd

        feCAEDetRequest.setImpTotal(detail.getImporte());
        feCAEDetRequest.setImpNeto(detail.getImporte()); // Para factura C = impTotal
        feCAEDetRequest.setImpTotConc(impTotConc); // Para factura C = 0
        feCAEDetRequest.setImpOpEx(impOpEx); // Para factura C = 0
        feCAEDetRequest.setImpIVA(impIVA); // Para factura C = 0

        feCAEDetRequest.setFchServDesde(detail.getFchServDesde());
        feCAEDetRequest.setFchServHasta(detail.getFchServHasta());
        feCAEDetRequest.setFchVtoPago(detail.getFchVtoPago());
        feCAEDetRequest.setMonId(monId); // Pesos Argentinos = PES
        feCAEDetRequest.setMonCotiz(monCotiz); // Para ARS = 1

        return feCAEDetRequest;
    }
}
