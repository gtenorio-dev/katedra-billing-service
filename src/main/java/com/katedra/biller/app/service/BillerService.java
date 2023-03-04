package com.katedra.biller.app.service;

import com.katedra.biller.app.client.gen.*;
import com.katedra.biller.app.dto.BillDetailDTO;
import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.entity.AccountEntity;
import com.katedra.biller.app.entity.BillEntity;
import com.katedra.biller.app.model.TicketAccess;
import com.katedra.biller.app.repository.BillRepository;
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

    private static final String APROBADO = "A";
    private static final String RECHAZADO = "R";
    private static final String PARCIAL = "P";

    @Autowired
    private AfipWSAAService wsaaService;
    @Autowired
    private AfipWSFEService wsfeService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private BillRepository billRepository;

    public FECAESolicitarResponse create(BillingPayload billingPayload) throws Exception {
        AccountEntity account = accountService.findByCuit(billingPayload.getCuit());
        FECAESolicitarResponse fecaeSolicitarResponse = wsfeService.generateBill(
                getFEAuthRequest(account), buildBillRequest(billingPayload, account));

        validateBill(account, fecaeSolicitarResponse.getFECAESolicitarResult(), billingPayload);

        return fecaeSolicitarResponse;
    }

    public FECompUltimoAutorizadoResponse getUltimoComprobanteAutorizado(Long cuit) throws Exception {
        AccountEntity account = accountService.findByCuit(cuit);
        return wsfeService.getUltimoComprobanteAutorizado(
                getFEAuthRequest(account), account.getPuntoVenta(), cbteTipo);
    }

    public FEParamGetPtosVentaResponse getPuntosVenta(Long cuit) throws Exception {
        AccountEntity account = accountService.findByCuit(cuit);
        return wsfeService.getPuntosVenta(getFEAuthRequest(account));
    }

    private void validateBill(AccountEntity account, FECAEResponse res, BillingPayload billingPayload) {
        switch (res.getFeCabResp().getResultado()) {
            case APROBADO:
                logger.info("APROBADO");
                res.getFeDetResp().getFECAEDetResponse().forEach(item -> {
                    saveBill(item, res.getFeCabResp().getFchProceso(), account);
                });
                break;
            case RECHAZADO:
                logger.info("RECHAZADO");
                break;
            case PARCIAL:
                logger.info("PARCIAL");
                res.getFeDetResp().getFECAEDetResponse().forEach(item -> {
                    if (APROBADO.equals(item.getResultado())) saveBill(item, res.getFeCabResp().getFchProceso(), account);
                });
                break;
        }
    }

    private void saveBill(FECAEDetResponse item, String fchProceso, AccountEntity account) {
        BillEntity bill = new BillEntity();
        bill.setFechaProceso(fchProceso);
        bill.setNumComprobante(item.getCbteDesde());
        bill.setFechaComprobante(item.getCbteFch());
        bill.setResultado(item.getResultado());
        bill.setDni(item.getDocNro());
        bill.setCae(item.getCAE());
        bill.setCaeFechaVto(item.getCAEFchVto());
        bill.setAccount(account);
        billRepository.save(bill);
        logger.info("CUIT del facturador: ".concat(account.getCuit().toString())
                .concat(" - Se genero con exito la factura con id ").concat(bill.getId().toString())
                .concat(" y CAE ").concat(bill.getCae()));
    }

    private String buildItemErrorMessage(List<Obs> obs) {
        StringBuilder errorMessages = new StringBuilder();
        obs.forEach(ob -> errorMessages.append(String.valueOf(
                ob.getCode()).concat(" - ").concat(ob.getMsg()).concat("\n")));
        return errorMessages.toString();
    }

    private String buildErrorMessage(List<Err> errors) {
        StringBuilder errorMessages = new StringBuilder();
        errors.forEach(err -> errorMessages.append(String.valueOf(err.getCode()).concat(" - ").concat(err.getMsg())));
        return errorMessages.toString();
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
            TicketAccess ta = wsaaService.authenticate(account);
            account = accountService.updateSession(account, ta);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date expirationTime = dateFormat.parse(account.getExpirationTime());
            Date currentDateTime = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expirationTime);
            calendar.add(Calendar.MINUTE, -10);
            Date expirationTimeLimit = calendar.getTime();

            if (expirationTime.compareTo(currentDateTime) < 0) {
                logger.info("Sesion vencida. Se obtiene una nueva");
                TicketAccess ta = wsaaService.authenticate(account);
                account = accountService.updateSession(account, ta);
            } else if (expirationTimeLimit.compareTo(currentDateTime) < 0) {
                logger.info("La sesion vence en menos de 10 minutos. Se obtiene una nueva");
                TicketAccess ta = wsaaService.authenticate(account);
                account = accountService.updateSession(account, ta);
            }
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
        fecaeCabRequest.setCbteTipo(cbteTipo); // cbteTipo = 11 = Factura C - Monotributo
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        feCAEDetRequest.setCbteFch(dateFormat.format(new Date())); // Formato yyyymmdd

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
