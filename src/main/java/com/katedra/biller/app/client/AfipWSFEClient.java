package com.katedra.biller.app.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.katedra.biller.app.client.gen.FEAuthRequest;
import com.katedra.biller.app.client.gen.FECAERequest;
import com.katedra.biller.app.client.gen.FECAESolicitar;
import com.katedra.biller.app.client.gen.FECAESolicitarResponse;
import com.katedra.biller.app.client.gen.FECompUltimoAutorizado;
import com.katedra.biller.app.client.gen.FEParamGetTiposCbte;
import com.katedra.biller.app.client.gen.FEParamGetTiposCbteResponse;

public class AfipWSFEClient extends WebServiceGatewaySupport {

	private static final Logger logger = LoggerFactory.getLogger(AfipWSFEClient.class);

	public FECAESolicitarResponse feCAESolicitar(FEAuthRequest feAuthRequest, FECAERequest feCAERequest) {

		FECAESolicitar request = new FECAESolicitar();
		request.setAuth(feAuthRequest);
		request.setFeCAEReq(feCAERequest);

		logger.info("FECAESolicitar Request: ".concat(request.toString()));

		FECAESolicitarResponse response = (FECAESolicitarResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request);

		return response;
	}

	public void feCompUltimoAutorizado(FEAuthRequest feAuthRequest) {

		FECompUltimoAutorizado request = new FECompUltimoAutorizado();
		request.setAuth(feAuthRequest);
		request.setPtoVta(2);
		request.setCbteTipo(0);

	}

	public void feParamGetTiposCbte(FEAuthRequest feAuthRequest) {
		FEParamGetTiposCbte request = new FEParamGetTiposCbte();
		request.setAuth(feAuthRequest);

		logger.info("FEParamGetTiposCbte Request: ".concat(request.toString()));

		// https://stackoverflow.com/questions/14571273/spring-ws-client-not-setting-soapaction-header

//		SoapActionCallback soapActionCallback = new SoapActionCallback("op=FEParamGetTiposCbte");

		FEParamGetTiposCbteResponse response = (FEParamGetTiposCbteResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request, null);

		System.out.println(response.getFEParamGetTiposCbteResult().getResultGet());
		System.out.println(response.getFEParamGetTiposCbteResult().getErrors());
		System.out.println(response.getFEParamGetTiposCbteResult().getEvents());
	}

}
