package com.akg.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SeDeMessage<T> {
	
	public String getJsonString(T t) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		return mapper.writeValueAsString(t);
	}
	
   public T getObject(String json, TypeReference typeReference) throws JsonParseException, JsonMappingException, IOException{
	      ObjectMapper mapper = new ObjectMapper();
	      mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
	     T t = (T) mapper.readValue(json, typeReference);
	      return t;
	   }
   
   public TypeReference getTypeReference() {
		return new TypeReference<T>() {};
	}
}
