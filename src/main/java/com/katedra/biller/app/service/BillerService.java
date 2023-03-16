package com.katedra.biller.app.service;

import com.katedra.biller.app.client.gen.*;
import com.katedra.biller.app.dto.*;
import com.katedra.biller.app.entity.AccountEntity;
import com.katedra.biller.app.entity.BillEntity;
import com.katedra.biller.app.entity.SessionEntity;
import com.katedra.biller.app.payload.BillingPayload;
import com.katedra.biller.app.repository.BillRepository;
import com.katedra.biller.app.utils.PDFGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BillerService {

    private static final Logger logger = LoggerFactory.getLogger(BillerService.class);
    private static final String APROBADO = "A";
    private static final String RECHAZADO = "R";
    private static final String PARCIAL = "P";
    private final BillRepository billRepository;
    private final AfipWSAAService wsaaService;
    private final AfipWSFEService wsfeService;
    private final AccountService accountService;
    private final SessionService sessionService;
    SimpleDateFormat dateFormat;
    SimpleDateFormat dateTimeFormat;
    SimpleDateFormat dateTimeZoneFormat;
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
    @Value("${afip.fe.qr.url}")
    private String qrUrl;

    public BillerService(AfipWSAAService wsaaService, AfipWSFEService wsfeService, AccountService accountService,
                         BillRepository billRepository, SessionService sessionService) {
        this.billRepository = billRepository;
        this.wsaaService = wsaaService;
        this.wsfeService = wsfeService;
        this.accountService = accountService;
        this.sessionService = sessionService;

        this.dateFormat = new SimpleDateFormat("yyyyMMdd");
        this.dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        this.dateTimeZoneFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    public BillProcess create(BillingPayload billingPayload) throws Exception {
        SessionEntity session = sessionService.getSession(billingPayload.getCuit());
        validateAccountLimits(session.getAccount(), billingPayload);
        int ultCbteAutorizado = wsfeService.getUltimoComprobanteAutorizado(getFEAuthRequest(session),
                session.getAccount().getPuntoVenta(), session.getAccount().getTipoFactura())
                .getFECompUltimoAutorizadoResult().getCbteNro();

        FECAESolicitarResponse fecaeSolicitarResponse =
                wsfeService.generateBill(getFEAuthRequest(session), buildBillRequest(billingPayload,
                        session.getAccount(), ultCbteAutorizado));

        return saveBills(session.getAccount(), fecaeSolicitarResponse.getFECAESolicitarResult(), billingPayload);
    }

    public FECompUltimoAutorizadoResponse getUltimoComprobanteAutorizado(Long cuit) throws Exception {
        SessionEntity session = sessionService.getSession(cuit);
        return wsfeService.getUltimoComprobanteAutorizado(getFEAuthRequest(session),
                session.getAccount().getPuntoVenta(), session.getAccount().getTipoFactura());
    }

    public FEParamGetPtosVentaResponse getPuntosVenta(Long cuit) throws Exception {
        SessionEntity session = sessionService.getSession(cuit);
        return wsfeService.getPuntosVenta(getFEAuthRequest(session));
    }

    public FECompConsultarResponse getBillInfo(Long cuit, Long numComprobante) throws Exception {
        SessionEntity session = sessionService.getSession(cuit);
        return getBillInfo(session, numComprobante);
    }

    public FECompConsultarResponse getBillInfo(SessionEntity session, Long numComprobante) throws Exception {
        FECompConsultaReq feCompConsultaReq = new FECompConsultaReq();
        feCompConsultaReq.setCbteNro(numComprobante);
        feCompConsultaReq.setPtoVta(session.getAccount().getPuntoVenta());
        feCompConsultaReq.setCbteTipo(session.getAccount().getTipoFactura());
        return wsfeService.getBill(getFEAuthRequest(session), feCompConsultaReq);
    }

    public ByteArrayInputStream buildFile(BillDTO billDTO) throws Exception {
        FECompConsultarResponse comprobante = getBillInfo(billDTO.getCuit(), billDTO.getNumComprobante());
        AccountEntity account = accountService.findByCuit(billDTO.getCuit());

        if (comprobante.getFECompConsultarResult().getResultGet() != null) {
            ByteArrayInputStream pdf =
                    PDFGenerator.generate(account, billDTO, comprobante.getFECompConsultarResult().getResultGet(), qrUrl);
            logger.info("Invoice PDF generated");
            return pdf;
        } else {
            // TODO throw not found exception ("El comprobante X no fue encontrado")
            String errorMsg = buildErrorMessage(comprobante.getFECompConsultarResult().getErrors().getErr());
            logger.error("Error generando el pdf. {}", errorMsg);
            throw new Exception(errorMsg);
        }
    }

    public String getPDFFileName(BillDTO billDTO) {
        AccountEntity account = accountService.findByCuit(billDTO.getCuit());
        return billDTO.getCuit().toString().concat(account.getPuntoVenta().toString()).concat(billDTO.getNumComprobante().toString()).concat(".pdf");
    }

    public BanlanceDTO getTotalBills(Long cuit, String sinceStr, String toStr) throws Exception {
        Date since;
        Date to;
        try {
            since = dateFormat.parse(sinceStr);
            to = dateFormat.parse(toStr);
        } catch (ParseException e) {
            // TODO return 400 Bad Request
            logger.error("El formato de las fechas es invalido");
            throw new Exception("El formato de las fechas es invalido");
        }

        return getTotalBills(cuit, since, to);
    }

    public BanlanceDTO getTotalBills(Long cuit, Date since, Date to) throws Exception {
        List<BillEntity> bills = billRepository.findBillsByCuitBetweenDates(cuit, since, to);

        BanlanceDTO res = new BanlanceDTO();
        res.setCuit(cuit);
        res.setFacturas((long) bills.size());
        res.setFacturado(bills.stream().mapToDouble(BillEntity::getImporte).sum());
        return res;
    }

    private BillProcess saveBills(AccountEntity account, FECAEResponse res, BillingPayload billingPayload) throws ParseException {
        BillProcess billProcess = new BillProcess();
        switch (res.getFeCabResp().getResultado()) {
            case APROBADO:
                logger.info("APROBADO");
                for (int i = 0; i < res.getFeDetResp().getFECAEDetResponse().size(); i++) {
                    FECAEDetResponse item = res.getFeDetResp().getFECAEDetResponse().get(i);
                    BillDetailDTO billItem = billingPayload.getDetails().get(i);
                    saveBill(item, res.getFeCabResp().getFchProceso(), account, billItem);

                    BillProcessDetail detail =
                            new BillProcessDetail(billItem.getVentaId(), item.getCbteDesde(), item.getCAE(), null);
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

                        BillProcessDetail detail =
                                new BillProcessDetail(billItem.getVentaId(), item.getCbteDesde(), item.getCAE(), null);
                        billProcess.addDetail(detail);
                    } else {
                        billingError(billProcess, item, billItem);
                    }
                }
                break;
        }
        billProcess.setError(buildErrorMessage(res.getErrors() != null ? res.getErrors().getErr() : null));
        return billProcess;
    }

    private void billingError(BillProcess billProcess, FECAEDetResponse item, BillDetailDTO billItem) {
        String errorMsg =
                item.getObservaciones() != null ? buildItemErrorMessage(item.getObservaciones().getObs()) : null;
        logger.error("Venta ".concat(billItem.getVentaId().toString()).concat(" - Estado: ").concat(item.getResultado()).concat(" - Mensaje: ").concat(errorMsg == null ? "" : errorMsg));

        BillProcessDetail detail = new BillProcessDetail(billItem.getVentaId(), null, null, errorMsg);
        billProcess.addDetail(detail);
    }

    private void saveBill(FECAEDetResponse item, String fchProceso, AccountEntity account, BillDetailDTO billItem) throws ParseException {
        BillEntity bill = new BillEntity();
        bill.setNumComprobante(item.getCbteDesde());
        bill.setDni(item.getDocNro());
        bill.setCae(item.getCAE());
        bill.setFechaProceso(dateTimeFormat.parse(fchProceso));
        bill.setFechaComprobante(dateFormat.parse(item.getCbteFch()));
        bill.setCaeFechaVto(dateFormat.parse(item.getCAEFchVto()));
        bill.setVentaId(billItem.getVentaId());

        bill.setAccount(account);

        try {
            FECompConsultarResponse comprobante = getBillInfo(account.getCuit(), bill.getNumComprobante());
            FECompConsResponse resultGet = comprobante.getFECompConsultarResult().getResultGet();
            bill.setFechaServicioDesde(dateFormat.parse(resultGet.getFchServDesde()));
            bill.setImporte(resultGet.getImpTotal());
        } catch (Exception e) {
            logger.error("No se pudo consultar la informacion de la Factura nro: ".concat(bill.getNumComprobante().toString()));
            bill.setImporte(billItem.getImporte());
        }

        billRepository.save(bill);
        logger.info("Venta Nro: ".concat(billItem.getVentaId().toString()).concat(" - CUIT del facturador: ").concat(account.getCuit().toString()).concat(" - Se generó con éxito la Factura Nro: ").concat(bill.getNumComprobante().toString()).concat(" con CAE ").concat(bill.getCae()));
    }

    private String buildItemErrorMessage(List<Obs> obs) {
        if (obs != null) {
            StringBuilder errorMessages = new StringBuilder();
            obs.forEach(ob -> errorMessages.append(String.valueOf(ob.getCode()).concat(" - ").concat(ob.getMsg()).concat("\n")));
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
     * @param session
     * @return FEAuthRequest
     * @throws Exception
     */
    private FEAuthRequest getFEAuthRequest(SessionEntity session) throws Exception {
        if (session.getExpirationTime() == null) {
            wsaaService.authenticate(session);
            sessionService.updateSession(session);
        } else {
            Date expirationTime = dateTimeZoneFormat.parse(session.getExpirationTime());
            Date currentDateTime = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expirationTime);
            calendar.add(Calendar.MINUTE, -10);
            Date expirationTimeLimit = calendar.getTime();

            if (expirationTime.compareTo(currentDateTime) < 0) {
                logger.info("Sesion vencida. Se obtiene una nueva");
                wsaaService.authenticate(session);
                sessionService.updateSession(session);
            } else if (expirationTimeLimit.compareTo(currentDateTime) < 0) {
                logger.info("La sesion vence en menos de 10 minutos. Se obtiene una nueva");
                wsaaService.authenticate(session);
                sessionService.updateSession(session);
            } else {
                logger.info("Sesion valida");
            }
        }
        FEAuthRequest authRequest = new FEAuthRequest();
        authRequest.setCuit(session.getAccount().getCuit());
        authRequest.setToken(session.getToken());
        authRequest.setSign(session.getSign());

        return authRequest;
    }

    private FECAERequest buildBillRequest(BillingPayload billingPayload, AccountEntity account, int ultCbteAutorizado) throws Exception {

        ArrayOfFECAEDetRequest arrayOfFECAEDetRequest = new ArrayOfFECAEDetRequest();
        List<FECAEDetRequest> fecaeDetRequests = arrayOfFECAEDetRequest.getFECAEDetRequest();

        for (BillDetailDTO detail : billingPayload.getDetails()) {
            FECAEDetRequest detRequest =
                    buildDetails(detail, account.getConcepto(), account.getPuntoVenta(), ++ultCbteAutorizado);
            fecaeDetRequests.add(detRequest);
        }

        FECAERequest fecaeRequest = new FECAERequest();
        fecaeRequest.setFeDetReq(arrayOfFECAEDetRequest);
        fecaeRequest.setFeCabReq(buildHeader(account.getPuntoVenta(), account.getTipoFactura(), fecaeDetRequests.size()));
        return fecaeRequest;
    }

    private FECAECabRequest buildHeader(int ptoVenta, int cbteTipo, int cantReg) {
        FECAECabRequest fecaeCabRequest = new FECAECabRequest();
        fecaeCabRequest.setPtoVta(ptoVenta); // Punto de venta 2
        fecaeCabRequest.setCbteTipo(cbteTipo); // cbteTipo = 11 = Factura C - Monotributo
        fecaeCabRequest.setCantReg(cantReg); // Cantidad de productos a facturar
        return fecaeCabRequest;
    }

    private FECAEDetRequest buildDetails(BillDetailDTO detail, int concepto, int ptoVenta, int numComprobante) throws Exception {
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


    private void validateAccountLimits(AccountEntity account, BillingPayload billingPayload) throws Exception{
        Date date = new Date();
        Date currentMonthFisrtDay = new Date(date.getYear(), date.getMonth(), 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentMonthFisrtDay);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date  currentMonthLastDay = calendar.getTime();

        double totalBilled = getTotalBills(account.getCuit(), currentMonthFisrtDay, currentMonthLastDay).getFacturado();
        double totalToBill = billingPayload.getDetails().stream().mapToDouble(BillDetailDTO::getImporte).sum();

        // TODO 400 BadRequest
        if (totalBilled + totalToBill > account.getLimite()) throw new Exception("No se puede facturar porque se va " +
                "asuperar el limite de $".concat(account.getLimite().toString()));
    }

}
