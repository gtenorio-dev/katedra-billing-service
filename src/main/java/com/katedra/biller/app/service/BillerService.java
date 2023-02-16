package com.katedra.biller.app.service;

import com.katedra.biller.app.client.gen.FEAuthRequest;
import com.katedra.biller.app.client.gen.FECAESolicitarResponse;
import com.katedra.biller.app.client.gen.FECompUltimoAutorizadoResponse;
import com.katedra.biller.app.client.gen.FEParamGetTiposCbteResponse;
import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.model.TicketAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BillerService {

    private static final Logger logger = LoggerFactory.getLogger(BillerService.class);

    @Autowired
    @Qualifier("authentications")
    Map<Long, TicketAccess> authentications;

    @Autowired
    private AfipWSAAService wsaaService;

    @Autowired
    private AfipWSFEService wsfeService;

    public FECAESolicitarResponse create(BillingPayload billingPayload) throws Exception {
        return wsfeService.generateBill(getFEAuthRequest(billingPayload.getCuit()));
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

}
