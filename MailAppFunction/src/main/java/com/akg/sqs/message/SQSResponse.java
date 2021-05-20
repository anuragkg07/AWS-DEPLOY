package com.akg.sqs.message;

import java.io.Serializable;
import java.util.Map;

public class SQSResponse<T> implements Serializable {
	private T body;
	private String jsonBody;
	private Map<String, String> attribute;
	private String md5OfBody;
	private String md5OfMessageAttributes;
	private String msgId;
	private String ReceiptHandle;
// Map<String, MessageAttributeValue> a5 = message.getMessageAttributes();

	public SQSResponse() {
		super();
	}

	public SQSResponse(T body, String jsonBody, Map<String, String> attribute, String md5OfBody,
			String md5OfMessageAttributes, String msgId, String receiptHandle) {
		super();
		this.body = body;
		this.jsonBody = jsonBody;
		this.attribute = attribute;
		this.md5OfBody = md5OfBody;
		this.md5OfMessageAttributes = md5OfMessageAttributes;
		this.msgId = msgId;
		ReceiptHandle = receiptHandle;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public String getJsonBody() {
		return jsonBody;
	}

	public void setJsonBody(String jsonBody) {
		this.jsonBody = jsonBody;
	}

	public Map<String, String> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, String> attribute) {
		this.attribute = attribute;
	}

	public String getMd5OfBody() {
		return md5OfBody;
	}

	public void setMd5OfBody(String md5OfBody) {
		this.md5OfBody = md5OfBody;
	}

	public String getMd5OfMessageAttributes() {
		return md5OfMessageAttributes;
	}

	public void setMd5OfMessageAttributes(String md5OfMessageAttributes) {
		this.md5OfMessageAttributes = md5OfMessageAttributes;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getReceiptHandle() {
		return ReceiptHandle;
	}

	public void setReceiptHandle(String receiptHandle) {
		ReceiptHandle = receiptHandle;
	}

}
