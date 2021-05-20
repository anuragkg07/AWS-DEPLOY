package com.akg.sqs.util;

import java.io.IOException;

import com.akg.sqs.message.SQSResponse;
import com.akg.util.SeDeMessage;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SQSConverter<T> {
	private SeDeMessage<T> sede = new SeDeMessage<T>();

	public  SQSResponse<T> get(Message message,TypeReference typeReference)
			throws JsonParseException, JsonMappingException, IOException {
		if (message != null) {
			//T body = sede.getObject(message.getBody(), typeReference);
			System.out.println("SQSConverter>>>>>> "+message.getBody());
			T body =null;
			SQSResponse<T> response = new SQSResponse<T>(body, 
					message.getBody(), 
					message.getAttributes(),
					message.getMD5OfBody(), 
					message.getMD5OfMessageAttributes(), 
					message.getMessageId(),
					message.getReceiptHandle());
			return response;
		}

		return null;

	}
	

}
