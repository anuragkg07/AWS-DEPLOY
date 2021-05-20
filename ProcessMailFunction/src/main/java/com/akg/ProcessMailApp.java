package com.akg;

import java.util.Map;

import com.akg.service.EmailService;
import com.akg.service.EmailServiceImpl;
import com.akg.sqs.message.SQSRequest;
import com.akg.to.EmailDetails;
import com.akg.util.SeDeMessage;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Handler for requests to Lambda function.
 */
public class ProcessMailApp implements RequestHandler<SQSEvent, Void> {

	private String mail_host = System.getenv("mail_host");
	private String mail_port = System.getenv("mail_port");
	private String mail_username = System.getenv("mail_username");
	private String mail_password = System.getenv("mail_password");
	private String mail_transport_protocol = System.getenv("mail_transport_protocol");
	private String mail_smtp_auth = System.getenv("mail_smtp_auth");
	private String mail_smtp_starttls_enable = System.getenv("mail_smtp_starttls_enable");
	private String mail_debug = System.getenv("mail_debug");
	private SeDeMessage<SQSRequest<EmailDetails>> sede = new SeDeMessage<SQSRequest<EmailDetails>>();
	private EmailService es = new EmailServiceImpl(mail_host, 
			mail_port, 
			mail_username, 
			mail_password,
			mail_transport_protocol, 
			mail_smtp_auth, 
			mail_smtp_starttls_enable, 
			mail_debug);



	@Override
	public Void handleRequest(SQSEvent event, Context context) {
		System.out.println("Entered ProcessMailApp : handleRequest");
		System.out.println("------------");
		
		try {
			for (SQSMessage msg : event.getRecords()) {
				System.out.println(msg);
				System.out.println("------*-------");
				System.out.println(new String(msg.getBody()));
				System.out.println("------**------");
				SQSRequest<EmailDetails>  request = sede.getObject(new String(msg.getBody()), getSQSRequestT());
				System.out.println("------***------");
				System.out.println(request);
				System.out.println("------****------");
				EmailDetails ed = request.getData();
				System.out.println("ed >>>> "+ed);
				Map<String, String> map = ed.getParamMap();
				System.out.println("map >>>> "+map);
				if(request!=null && ed!=null && map!=null) {
					System.out.println(mail_username+"------------"+mail_password);
					es.send(map.get("toEmail"));					
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		System.out.println("Exited ProcessMailApp : handleRequest");

		return null;
	}
	
	
	private TypeReference getSQSRequestT() {
		return new TypeReference<SQSRequest<EmailDetails>>() {};
	}
	
}
