package com.akg.to;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;


public class EmailDetails implements Serializable {
private Map<String,String> paramMap;

public EmailDetails() {
	super();
	this.paramMap = new HashMap<String, String>();
}

@JsonProperty("map")
public Map<String, String> getParamMap() {
	return paramMap;
}

public void setParamMap(Map<String, String> paramMap) {
	this.paramMap = paramMap;
}

@Override
public String toString() {
	return "EmailDetails [paramMap=" + paramMap + "]";
}

}
