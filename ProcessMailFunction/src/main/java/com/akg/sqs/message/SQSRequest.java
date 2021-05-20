package com.akg.sqs.message;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

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
	@JsonGetter("data")
	public T getData() {
		return data;
	}
	@JsonSetter("data")
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
		   "msgId":"Myid1",
		   "msgGroupId":"MyGrpId",
		   "data":{
		      "paramMap":{
		         "toEmail":"anurag@gmail.com",
		         "fromEmail":"akg@gmail.com"
		      }
		   }
		}
	*/
	
}
