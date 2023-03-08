package com.katedra.biller.app.service;

import com.katedra.biller.app.client.gen.*;
import com.katedra.biller.app.dto.*;
import com.katedra.biller.app.entity.AccountEntity;
import com.katedra.biller.app.entity.BillEntity;
import com.katedra.biller.app.model.TicketAccess;
import com.katedra.biller.app.repository.BillRepository;
import com.katedra.biller.app.utils.PDFGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BillerService {

    private static final Logger logger = LoggerFactory.getLogger(BillerService.class);
    private final AfipWSAAService wsaaService;
    private final AfipWSFEService wsfeService;
    private final AccountService accountService;
    private final BillRepository billRepository;

    public BillerService(AfipWSAAService wsaaService, AfipWSFEService wsfeService, AccountService accountService,
                         BillRepository billRepository) {
        this.wsaaService = wsaaService;
        this.wsfeService = wsfeService;
        this.accountService = accountService;
        this.billRepository = billRepository;
    }

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

    public BillProcess create(BillingPayload billingPayload) throws Exception {
        AccountEntity account = accountService.findByCuit(billingPayload.getCuit());
        FECAESolicitarResponse fecaeSolicitarResponse = wsfeService.generateBill(
                getFEAuthRequest(account), buildBillRequest(billingPayload, account));
        return saveBills(account, fecaeSolicitarResponse.getFECAESolicitarResult(), billingPayload);
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

    public FECompConsultarResponse getBillInfo(AccountEntity account, Long numComprobante) throws Exception {
        FECompConsultaReq feCompConsultaReq = new FECompConsultaReq();
        feCompConsultaReq.setCbteNro(numComprobante);
        feCompConsultaReq.setPtoVta(account.getPuntoVenta());
        feCompConsultaReq.setCbteTipo(cbteTipo);
        return wsfeService.getBill(getFEAuthRequest(account), feCompConsultaReq);
    }

    public FECompConsultarResponse getBillInfo(Long cuit, Long numComprobante) throws Exception {
        AccountEntity account = accountService.findByCuit(cuit);
        return getBillInfo(account, numComprobante);
    }

    public ByteArrayInputStream buildFile(BillDTO billDTO) throws Exception {
        AccountEntity account = accountService.findByCuit(billDTO.getCuit());
        FECompConsultarResponse comprobante = getBillInfo(account, billDTO.getNumComprobante());

        if (comprobante.getFECompConsultarResult().getResultGet() != null) {
            ByteArrayInputStream pdf = PDFGenerator.generate(account, billDTO, comprobante.getFECompConsultarResult().getResultGet());
            logger.info("Invoice PDF generated");
            return pdf;
        } else {
            // TODO throw not found exception
            throw new Exception(buildErrorMessage(comprobante.getFECompConsultarResult().getErrors().getErr()));
        }
    }

    public String getPDFFileName(BillDTO billDTO) {
        AccountEntity account = accountService.findByCuit(billDTO.getCuit());
        return billDTO.getCuit().toString().concat(account.getPuntoVenta().toString())
                .concat(billDTO.getNumComprobante().toString());
    }

    public Double getTotalBills(Long cuit, Date since, Date to) {

        System.out.println("TODO");

        return 12345D;
    }

    private BillProcess saveBills(AccountEntity account, FECAEResponse res, BillingPayload billingPayload) {
        BillProcess billProcess = new BillProcess();
        switch (res.getFeCabResp().getResultado()) {
            case APROBADO:
                logger.info("APROBADO");
                for (int i = 0; i < res.getFeDetResp().getFECAEDetResponse().size(); i++) {
                    FECAEDetResponse item = res.getFeDetResp().getFECAEDetResponse().get(i);
                    BillDetailDTO billItem = billingPayload.getDetails().get(i);
                    saveBill(item, res.getFeCabResp().getFchProceso(), account, billItem);

                    BillProcessDetail detail = new BillProcessDetail(billItem.getVentaId(),item.getCbteDesde(), item.getCAE(), null);
                    billProcess.addDetail(detail);
                }
                break;
            case RECHAZADO:
                logger.info("RECHAZADO");
                for (int i = 0; i < res.getFeDetResp().getFECAEDetResponse().size(); i++) {
                    FECAEDetResponse item = res.getFeDetResp().getFECAEDetResponse().get(i);
                    BillDetailDTO billItem = billingPayload.getDetails().get(i);
                    billingError(billProcess, item, billItem);
                }
                break;
            case PARCIAL:
                logger.info("PARCIAL");
                for (int i = 0; i < res.getFeDetResp().getFECAEDetResponse().size(); i++) {
                    FECAEDetResponse item = res.getFeDetResp().getFECAEDetResponse().get(i);
                    BillDetailDTO billItem = billingPayload.getDetails().get(i);
                    if (APROBADO.equals(item.getResultado())) {
                        saveBill(item, res.getFeCabResp().getFchProceso(), account, billItem);

                        BillProcessDetail detail = new BillProcessDetail(billItem.getVentaId(),item.getCbteDesde(), item.getCAE(), null);
                        billProcess.addDetail(detail);
                    } else {
                        billingError(billProcess, item, billItem);
                    }
                }
                break;
        }
        billProcess.setError(buildErrorMessage(res.getErrors() != null? res.getErrors().getErr(): null));
        return billProcess;
    }

    private void billingError(BillProcess billProcess, FECAEDetResponse item, BillDetailDTO billItem) {
        String errorMsg = item.getObservaciones() != null? buildItemErrorMessage(item.getObservaciones().getObs()): null;
        logger.error("Venta ".concat(billItem.getVentaId().toString()).concat(" - Estado: ")
                .concat(item.getResultado()).concat(" - Mensaje: ")
                .concat(errorMsg == null? "" : errorMsg));

        BillProcessDetail detail = new BillProcessDetail(billItem.getVentaId(), null, null, errorMsg);
        billProcess.addDetail(detail);
    }

    private void saveBill(FECAEDetResponse item, String fchProceso, AccountEntity account, BillDetailDTO billItem) {
        BillEntity bill = new BillEntity();
        bill.setFechaProceso(fchProceso);
        bill.setNumComprobante(item.getCbteDesde());
        bill.setFechaComprobante(item.getCbteFch());
        bill.setDni(item.getDocNro());
        bill.setCae(item.getCAE());
        bill.setCaeFechaVto(item.getCAEFchVto());
        bill.setAccount(account);

        try {
            FECompConsultarResponse comprobante = getBillInfo(account, bill.getNumComprobante());
            bill.setImporte(comprobante.getFECompConsultarResult().getResultGet().getImpTotal());
        } catch (Exception e) {
            logger.error("No se pudo consultar la informacion de la Factura nro: ".concat(bill.getNumComprobante().toString()));
            bill.setImporte(billItem.getImporte());
        }

        billRepository.save(bill);
        logger.info("Venta Nro: ".concat(billItem.getVentaId().toString()).concat(" - CUIT del facturador: ")
                .concat(account.getCuit().toString()).concat(" - Se generó con éxito la Factura Nro: ")
                .concat(bill.getNumComprobante().toString()).concat(" con CAE ").concat(bill.getCae()));
    }

    private String buildItemErrorMessage(List<Obs> obs) {
        if (obs != null) {
            StringBuilder errorMessages = new StringBuilder();
            obs.forEach(ob -> errorMessages.append(String.valueOf(
                    ob.getCode()).concat(" - ").concat(ob.getMsg()).concat("\n")));
            return errorMessages.toString();
        } else {
            return null;
        }
    }

    private String buildErrorMessage(List<Err> errors) {
        if (errors != null) {
            StringBuilder errorMessages = new StringBuilder();
            errors.forEach(err -> errorMessages.append(String.valueOf(err.getCode()).concat(" - ").concat(err.getMsg())));
            return errorMessages.toString();
        } else {
            return null;
        }
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
