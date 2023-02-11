package com.katedra.biller.app.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.katedra.biller.app.client.gen.LoginCms;
import com.katedra.biller.app.client.gen.LoginCmsResponse;

public class AfipWSAAClient extends WebServiceGatewaySupport {

	private static final Logger logger = LoggerFactory.getLogger(WebServiceGatewaySupport.class);
	
	public LoginCmsResponse loginCms(String value) {
		LoginCms request = new LoginCms();
		request.setIn0(value);
		
		logger.info("LoginCms Request: ".concat(request.toString()));
		
		LoginCmsResponse response  = (LoginCmsResponse) getWebServiceTemplate().marshalSendAndReceive(request);
		
		return response;
	}
	
}
