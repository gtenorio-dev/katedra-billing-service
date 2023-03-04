package com.katedra.biller.app.service;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import com.katedra.biller.app.entity.AccountEntity;
import org.apache.axis.encoding.Base64;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.katedra.biller.app.client.AfipWSAAClient;
import com.katedra.biller.app.client.gen.LoginCmsResponse;
import com.katedra.biller.app.model.TicketAccess;
import com.katedra.biller.app.utils.AfipWSAAUtils;

@Service
public class AfipWSAAService {

	private static final Logger logger = LoggerFactory.getLogger(AfipWSAAService.class);

	@Autowired
	private AfipWSAAClient afipWSAAClient;

	@Value("${afip.wsaa.service}")
	private String service;

	@Value("${afip.wsaa.dstdn}")
	private String dstDN;
	@Value("${afip.wsaa.ticket-time}")
	private Long ticketTime;

//	@Value("${afip.wsaa.keystore}")
//	private String p12file;

//	@Value("${afip.wsaa.keystore.signer}")
//	private String signer;

//	@Value("${afip.wsaa.keystore.password}")
//	private String p12pass;


	public TicketAccess authenticate(AccountEntity account) throws Exception {
		TicketAccess ta = new TicketAccess();
		byte[] LoginTicketRequest_xml_cms = null;
		try {
			LoginTicketRequest_xml_cms = AfipWSAAUtils.create_cms(account, dstDN, service, ticketTime);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Error generando el Login Ticket Request XML CMS", e);
		}

		LoginCmsResponse response = null;
		try {
			response = afipWSAAClient.loginCms(Base64.encode(LoginTicketRequest_xml_cms));
		} catch (Exception e) {
			logger.error("Error autenticando. ".concat(e.getMessage()));
			throw new Exception(e.getMessage());
		}

		Map<String, String> authentication = new HashMap<>();
		try {
			Reader tokenReader = new StringReader(response.getLoginCmsReturn());
			Document tokenDoc = new SAXReader(false).read(tokenReader);

			String token = tokenDoc.valueOf("/loginTicketResponse/credentials/token");
			String sign = tokenDoc.valueOf("/loginTicketResponse/credentials/sign");
			String expirationTime = tokenDoc.valueOf("/loginTicketResponse/header/expirationTime");

			logger.info("TOKEN: ".concat(token));
			logger.info("SIGN: ".concat(sign));
			logger.info("ExpirationTime : ".concat(expirationTime));

			ta.setToken(token);
			ta.setSign(sign);
			ta.setExpirationTime(expirationTime);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Error extrayendo el Ticket de Acceso (TA)");
		}

		return ta;
	}

}
