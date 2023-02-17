package com.katedra.biller.app.client;

import com.katedra.biller.app.client.gen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class AfipWSFEClient extends WebServiceGatewaySupport {

	@Value("${afip.wsfe.soap.action}")
	private String soapAction;

	private static final Logger logger = LoggerFactory.getLogger(AfipWSFEClient.class);

	public FECAESolicitarResponse feCAESolicitar(FECAESolicitar request) {
		logger.info("Calling to FECAESolicitar");

		return (FECAESolicitarResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request, new SoapActionCallback(soapAction.concat("FECAESolicitar")));
	}

	public FECompUltimoAutorizadoResponse feCompUltimoAutorizado(FECompUltimoAutorizado request) {
		logger.info("Calling to FECompUltimoAutorizado");

		return (FECompUltimoAutorizadoResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request, new SoapActionCallback(soapAction.concat("FECompUltimoAutorizado")));

	}

	public FEParamGetTiposCbteResponse feParamGetTiposCbte(FEParamGetTiposCbte request) {
		logger.info("Calling to FEParamGetTiposCbte");

		return (FEParamGetTiposCbteResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request, new SoapActionCallback(soapAction.concat(request.getClass().getSimpleName())));
	}

}
