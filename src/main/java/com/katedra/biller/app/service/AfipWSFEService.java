package com.katedra.biller.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.katedra.biller.app.client.AfipWSFEClient;
import com.katedra.biller.app.client.gen.FEAuthRequest;
import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.model.TicketAccess;

@Service
public class AfipWSFEService {

	@Autowired
	private AfipWSFEClient wsfeClient;

	public String generateBill(TicketAccess ta, BillingPayload billingPayload) {
		
		FEAuthRequest feAuthRequest = new FEAuthRequest();
		feAuthRequest.setCuit(billingPayload.getCuit());
		feAuthRequest.setToken(ta.getToken());
		feAuthRequest.setSign(ta.getSign());
		
		wsfeClient.feCAESolicitar(feAuthRequest, null);

		return "testing";
	}
	
	public void getTiposComprobantes(TicketAccess ta, BillingPayload billingPayload) {
		wsfeClient.feParamGetTiposCbte(buildFEAuthRequest(ta, billingPayload.getCuit()));
	}
	
	private FEAuthRequest buildFEAuthRequest(TicketAccess ta, Long cuit) {
		FEAuthRequest feAuthRequest = new FEAuthRequest();
		feAuthRequest.setCuit(cuit);
		feAuthRequest.setToken(ta.getToken());
		feAuthRequest.setSign(ta.getSign());
		return feAuthRequest;
	}

}
