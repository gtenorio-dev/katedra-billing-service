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

	public FECAESolicitarResponse feCAESolicitar(FEAuthRequest feAuthRequest, FECAERequest feCAERequest) {

		FECAESolicitar request = new FECAESolicitar();
		request.setAuth(feAuthRequest);
		request.setFeCAEReq(feCAERequest);

		logger.info("Calling to FECAESolicitar");

		System.out.println("SOAP ACTION: ".concat(soapAction.concat(request.getClass().getSimpleName())));

		FECAESolicitarResponse response = (FECAESolicitarResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request, new SoapActionCallback(soapAction.concat("FECAESolicitar")));
		return response;
	}

	public FECompUltimoAutorizadoResponse feCompUltimoAutorizado(FEAuthRequest feAuthRequest, int ptoVenta, int cbteTipo) {

		FECompUltimoAutorizado request = new FECompUltimoAutorizado();
		request.setAuth(feAuthRequest);
		request.setPtoVta(ptoVenta);
		request.setCbteTipo(cbteTipo);

		logger.info("Calling to FECompUltimoAutorizado");

		return (FECompUltimoAutorizadoResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request, new SoapActionCallback(soapAction.concat("FECompUltimoAutorizado")));

	}

	public FEParamGetTiposCbteResponse feParamGetTiposCbte(FEAuthRequest feAuthRequest) {
		FEParamGetTiposCbte request = new FEParamGetTiposCbte();
		request.setAuth(feAuthRequest);

		logger.info("Calling to FEParamGetTiposCbte");
		System.out.println("SOAP ACTION: ".concat(soapAction.concat(request.getClass().getSimpleName())));

		return (FEParamGetTiposCbteResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request, new SoapActionCallback(soapAction.concat("FEParamGetTiposCbte")));
	}

}
