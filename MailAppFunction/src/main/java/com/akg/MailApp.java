package com.akg;

import com.akg.sqs.message.SQSRequest;
import com.akg.sqs.util.AmazonSQSUtil;
import com.akg.to.EmailDetails;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class MailApp {

	public static String EMAIL_QUEUE_NAME = System.getenv("EMAIL_QUEUE_NAME");
	AmazonSQSUtil<EmailDetails> sqsUtil=new AmazonSQSUtil<EmailDetails>(EMAIL_QUEUE_NAME, EmailDetails.class); 	
	
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request) {
    	System.out.println("Entered MailApp handleRequest");

		sqsUtil.listQueues();		
		SQSRequest<EmailDetails> sqsRequest = sqsUtil.getSQSRequest(request.getBody());		
		int status=sqsUtil.sendMessage(sqsRequest);
		
		APIGatewayProxyResponseEvent response= new APIGatewayProxyResponseEvent();
		response.withStatusCode(201);	
		response.withBody("status : "+status);
		
		System.out.println("Exited MailApp handleRequest");
		return response;
    }

    

}
