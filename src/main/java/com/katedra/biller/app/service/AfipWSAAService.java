package com.katedra.biller.app.service;

import com.katedra.biller.app.client.AfipWSAAClient;
import com.katedra.biller.app.client.gen.LoginCmsResponse;
import com.katedra.biller.app.entity.SessionEntity;
import com.katedra.biller.app.utils.AfipWSAAUtils;
import org.apache.axis.encoding.Base64;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.io.StringReader;

@Service
public class AfipWSAAService {

    private static final Logger logger = LoggerFactory.getLogger(AfipWSAAService.class);

    private final AfipWSAAClient afipWSAAClient;

    @Value("${afip.wsaa.service}")
    private String service;

    @Value("${afip.wsaa.dstdn}")
    private String dstDN;
    @Value("${afip.wsaa.ticket-time}")
    private Long ticketTime;

    public AfipWSAAService(AfipWSAAClient afipWSAAClient) {
        this.afipWSAAClient = afipWSAAClient;
    }


    public void authenticate(SessionEntity session) throws Exception {
        byte[] LoginTicketRequest_xml_cms;
        try {
            LoginTicketRequest_xml_cms = AfipWSAAUtils.create_cms(session.getCertName(), session.getCertSigner(),
                    session.getCertPassword(), dstDN, service, ticketTime);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("Error generando el Login Ticket Request XML CMS", e);
        }

        LoginCmsResponse response;
        try {
            response = afipWSAAClient.loginCms(Base64.encode(LoginTicketRequest_xml_cms));
        } catch (Exception e) {
            logger.error("Error autenticando. ".concat(e.getMessage()));
            throw new Exception(e.getMessage());
        }

        try {
            Reader tokenReader = new StringReader(response.getLoginCmsReturn());
            Document tokenDoc = new SAXReader(false).read(tokenReader);

            String token = tokenDoc.valueOf("/loginTicketResponse/credentials/token");
            String sign = tokenDoc.valueOf("/loginTicketResponse/credentials/sign");
            String expirationTime = tokenDoc.valueOf("/loginTicketResponse/header/expirationTime");

            logger.info("TOKEN: ".concat(token));
            logger.info("SIGN: ".concat(sign));
            logger.info("ExpirationTime : ".concat(expirationTime));

            session.setToken(token);
            session.setSign(sign);
            session.setExpirationTime(expirationTime);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("Error extrayendo el Ticket de Acceso (TA)");
        }
    }

}
