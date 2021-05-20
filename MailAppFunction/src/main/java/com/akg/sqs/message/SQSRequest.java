package com.akg.sqs.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonPropertyOrder({ "msgId", "msgGroupId", "data" })
@JsonRootName(value = "request")
public class SQSRequest<T> implements Serializable {

	
	protected T data;
	protected String msgId;
	protected String msgGroupId;

	public SQSRequest() {
		super();
	}

	public SQSRequest(T data, String msgId) {
		super();
		this.data = data;
		this.msgId = msgId;
	}
	
	@JsonCreator
	public SQSRequest(@JsonProperty("data") T data, @JsonProperty("msgId") String msgId, @JsonProperty("msgGroupId") String msgGroupId) {
		super();
		this.data = data;
		this.msgId = msgId;
		this.msgGroupId = msgGroupId;
	}

	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMsgGroupId() {
		return msgGroupId;
	}

	public void setMsgGroupId(String msgGroupId) {
		this.msgGroupId = msgGroupId;
	}

	@Override
	public String toString() {
		return "SQSRequest [data=" + data + ", msgId=" + msgId + ", msgGroupId=" + msgGroupId + "]";
	}

	
	/*
	{
   "request":{
      "msgId":"MyMsgId1",
      "msgGroupId":"MyGroupId1",
      "data":{
         "map":{
            "message":"Hi, How are you?",
            "toEmail":"akg@gmail.com",
            "fromEmail":"noReply@gmail.com"
         }
      }
   }
}
	*/
	
}
