package com.katedra.biller.app.service;

import com.katedra.biller.app.client.gen.*;
import com.katedra.biller.app.dto.BillDetailDTO;
import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.model.TicketAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BillerService {

    private static final Logger logger = LoggerFactory.getLogger(BillerService.class);

    private static final String APROBADO = "A";
    private static final String RECHAZADO = "R";
    private static final String PARCIAL = "P";

    @Autowired
    @Qualifier("authentications")
    Map<Long, TicketAccess> authentications;

    @Autowired
    private AfipWSAAService wsaaService;
    @Autowired
    private AfipWSFEService wsfeService;
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
        FECAESolicitarResponse fecaeSolicitarResponse = wsfeService.generateBill(getFEAuthRequest(billingPayload.getCuit()), buildBillRequest(billingPayload));

        // TODO Split response

        validateBill(fecaeSolicitarResponse.getFECAESolicitarResult());


        return wsfeService.generateBill(getFEAuthRequest(billingPayload.getCuit()), buildBillRequest(billingPayload));
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
        return wsfeService.getUltimoComprobanteAutorizado(getFEAuthRequest(cuit), 2, 11);
    }


    /**
     * Devuelve una Autenticacion valida para usar el servicio de Facturacion Electronica
     *
     * @param cuit
     * @return
     * @throws Exception
     */
    private FEAuthRequest getFEAuthRequest(Long cuit) throws Exception {
        TicketAccess ta = authentications.get(cuit);

        if (ta.getToken() == null) {
            wsaaService.authenticate(ta);
        } else {
            // TODO si el tiempo que le queda al TA es corto (menor a 30/60 mins)=> solicitar nuevo TA
            // TODO Case Validate ExpirationTime < Current Time
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//			Date date = df.parse(ta.getExpirationTime());
//			Calendar expirationDate = Calendar.getInstance();
//			expirationDate.setTime(date);
//
//			//TODO get Current Date
//			Calendar currentDate = Calendar.getInstance();

            // Testing Facturacion Electronica
//			wsfeService.generateBill(ta, billingPayload);

            System.out.println("---------");

            //TODO Exception --> No se puede obtener el Ticket de Acceso
        }

        FEAuthRequest authRequest = new FEAuthRequest();
        authRequest.setCuit(cuit);
        authRequest.setToken(ta.getToken());
        authRequest.setSign(ta.getSign());

        return authRequest;
    }


    private FECAERequest buildBillRequest(BillingPayload billingPayload) throws Exception {
        FECAERequest fecaeRequest = new FECAERequest();

        // TODO consultar en DB datos del emisor
        int ptoVenta = 2;

        int catRegistros = billingPayload.getDetails().size();

        ArrayOfFECAEDetRequest arrayOfFECAEDetRequest = new ArrayOfFECAEDetRequest();
        List<FECAEDetRequest> fecaeDetRequests = arrayOfFECAEDetRequest.getFECAEDetRequest();

        int ultimoComprobanteAutorizado = wsfeService
                .getUltimoComprobanteAutorizado(getFEAuthRequest(billingPayload.getCuit()), ptoVenta, cbteTipo)
                .getFECompUltimoAutorizadoResult().getCbteNro();

        for (BillDetailDTO detail : billingPayload.getDetails()) {
            FECAEDetRequest detRequest = buildDetails(detail, ptoVenta, ++ultimoComprobanteAutorizado);
            fecaeDetRequests.add(detRequest);
        }

        fecaeRequest.setFeDetReq(arrayOfFECAEDetRequest);
        fecaeRequest.setFeCabReq(buildHeader(ptoVenta, fecaeDetRequests.size()));
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
