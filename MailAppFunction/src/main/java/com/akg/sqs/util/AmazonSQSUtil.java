package com.akg.sqs.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.akg.sqs.message.SQSRequest;
import com.akg.sqs.message.SQSResponse;
import com.akg.util.SeDeMessage;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueResult;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AmazonSQSUtil<T> {

	private AmazonSQS sqsClient = null;
	private String queueName = null;
	private String queueUrl = null;
	private SeDeMessage<SQSRequest<T>> sede = null;
	private Class<T> clazzType;
	private SQSConverter<SQSResponse> coverter=null;
	private int delaySeconds;
	
	private boolean batch_flag = true;
	private boolean result_flag = true;

	public AmazonSQSUtil(String queueName, Class<T> clazzType) {
		super();
		this.queueName = queueName;
		this.clazzType = clazzType;
		init();
	}

	private void init() {
		sede = new SeDeMessage<SQSRequest<T>>();
		getAmazonSQSClient();
		getAmazonSQSUrl(queueName);
		coverter=new SQSConverter<SQSResponse>();
	}

	public AmazonSQS getAmazonSQSClient() {
		sqsClient = AmazonSQSClientBuilder.defaultClient();
		return sqsClient;
	}

	public void getAmazonSQSUrl(String queueName) {
		queueUrl = sqsClient.getQueueUrl(queueName).getQueueUrl();
	}

	public void listQueues() {
		ListQueuesResult lq_result = sqsClient.listQueues();
		System.out.println("Your SQS Queue URLs:");
		for (String url : lq_result.getQueueUrls()) {
			System.out.println(url);
		}
	}

	public boolean createSQueueS(String queueName, int messageRetentionPeriod) {
		boolean flag = false;
		CreateQueueRequest create_request = new CreateQueueRequest(queueName)
				.addAttributesEntry("DelaySeconds", delaySeconds + "")
				.addAttributesEntry("MessageRetentionPeriod", messageRetentionPeriod + "");

		try {
			sqsClient.createQueue(create_request);
			flag = true;
		} catch (AmazonSQSException e) {
			if (!e.getErrorCode().equals("QueueAlreadyExists")) {
				// throw e;
			}
			flag = false;
		}
		return flag;
	}

	public int deleteQueue(String queueName) {
		AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
		DeleteQueueResult result = sqs.deleteQueue(queueName);
		return this.getStatusCode(result);
	}

	public int sendMessage(SQSRequest<T> request) {
		SendMessageResult result = null;
		try {
			SendMessageRequest send_msg_request;
			send_msg_request = new SendMessageRequest()
					.withQueueUrl(queueUrl)
					.withMessageBody(getJsonString(request))
					.withDelaySeconds(this.delaySeconds);
			if(queueName.contains(".fifo")) {
				// if queue is fifo
				send_msg_request.withMessageGroupId(request.getMsgGroupId());
				send_msg_request.withMessageDeduplicationId(request.getMsgId());
			}
			
			result = sqsClient.sendMessage(send_msg_request);

		} catch (Exception e) {
			System.out.println("Error " + e);
		}
		return this.getStatusCode(result);

	}

	public int sendMessageList(List<SQSRequest<T>> requestList, String queueName) throws Exception {
		batch_flag = true;
		SendMessageBatchResult result = null;
		List<SendMessageBatchRequestEntry> messageList = new ArrayList<SendMessageBatchRequestEntry>();
		requestList.stream().forEach(x -> {
			try {
				SendMessageBatchRequestEntry sendMessageBatchRequestEntry = new SendMessageBatchRequestEntry(x.getMsgId(), getJsonString(x));
				if(queueName.contains(".fifo")) {
					// if queue is fifo
					sendMessageBatchRequestEntry.withMessageGroupId(x.getMsgGroupId());
					sendMessageBatchRequestEntry.withMessageDeduplicationId(x.getMsgId());
				}
				messageList.add(sendMessageBatchRequestEntry);
			} catch (Exception e) {
				if (batch_flag) {
					batch_flag = false;
				}
				System.out.println(e);
			}

		});
		if (batch_flag) {
			SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
					.withQueueUrl(this.queueUrl)
					.withEntries(messageList);
			
			result = sqsClient.sendMessageBatch(send_batch_request);

		} else {
			// Raise alarm
			System.out.println("Error while parsing");
		}
		return this.getStatusCode(result);
	}

	private int getStatusCode(com.amazonaws.AmazonWebServiceResult<com.amazonaws.ResponseMetadata> result) {
		if (result != null && result.getSdkHttpMetadata() != null) {
			return result.getSdkHttpMetadata().getHttpStatusCode();
		}
		return 0;
	}

	private String getJsonString(SQSRequest<T> request)
			throws JsonGenerationException, JsonMappingException, IOException {
		return sede.getJsonString(request);
	}

	public List<Message> receiveMessages() {
		List<Message> messages = sqsClient.receiveMessage(this.queueUrl).getMessages();		
		return messages;
	}
	
	public List<SQSResponse> receiveMessagesAsSQSResponse() {
		result_flag=true;
		List<SQSResponse> list = new ArrayList<SQSResponse>();
		List<Message> messages = this.receiveMessages();
		messages.stream().forEach(x->{
			try {
				list.add((SQSResponse) coverter.get(x, null));
			} catch (Exception e) {
				result_flag=false;
				System.out.println(e);
			}
		});
		if(result_flag==false) {
			// Raise alarm
			System.out.println("Error while parsing");
		}
		return list;
	}

	public int getDelaySeconds() {
		return delaySeconds;
	}

	public void setDelaySeconds(int delaySeconds) {
		this.delaySeconds = delaySeconds;
	}
	
	public SQSRequest<T> getSQSRequest(String body){
		try {
			SQSRequest<T> req= sede.getObject(body, this.getSQSRequestT());
			return req;
		} catch (IOException e) {
			// Raise alarm
			System.out.println("Error while parsing "+e);
		}
		return null;
	}
	
	private TypeReference getSQSRequestT() {
		return new TypeReference<SQSRequest<T>>() {};
	}
	
	   

}
