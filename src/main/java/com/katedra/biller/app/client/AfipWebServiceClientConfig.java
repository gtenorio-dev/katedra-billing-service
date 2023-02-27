package com.katedra.biller.app.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.katedra.biller.app.model.TicketAccess;

@Configuration
public class AfipWebServiceClientConfig {

	@Value("${afip.client.config.contextPath}")
	private String contextPath;

	@Value("${afip.wsfe.endpoint}")
	private String wsfeEndpoint;

	@Value("${afip.wsaa.endpoint}")
	private String wsaaEndpoint;

	@Value("#{'${cuits}'.split(',')}")
	private List<Long> cuits;
	
	@Bean
	public Jaxb2Marshaller marshaller()  {
	Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
	marshaller.setContextPath("com.katedra.biller.app.client.gen");
	return marshaller;
	}
	
	@Bean
	public AfipWSFEClient afipWSFEClient(Jaxb2Marshaller marshaller) {
		AfipWSFEClient client = new AfipWSFEClient();
		client.setDefaultUri(wsfeEndpoint);
		client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
	}
	
	@Bean
	public AfipWSAAClient afipWSAAClient(Jaxb2Marshaller marshaller) {
		AfipWSAAClient client = new AfipWSAAClient();
		client.setDefaultUri(wsaaEndpoint);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
        return client;
	}
	
//	@Bean
//	@Qualifier("authentications")
//	public Map<Long, TicketAccess> authentications(){
//		Map<Long, TicketAccess> auths = new HashMap<>();
//		cuits.forEach(c -> auths.put(c, new TicketAccess()));
//		return auths;
//	}
}
