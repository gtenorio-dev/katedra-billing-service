package com.katedra.biller.app.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.katedra.biller.app.dto.BillingPayload;
import com.katedra.biller.app.model.TicketAccess;

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

	public String create(BillingPayload billingPayload) throws Exception {

		TicketAccess ta = authentications.get(billingPayload.getCuit());

		if (ta.getToken() == null) {
			wsaaService.authenticate(ta);
		} else {
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

		}

		// Testing
		wsfeService.getTiposComprobantes(ta, billingPayload);

		return "TODO";
	}

}
