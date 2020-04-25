package com.myretail.catalog.product.ws.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myretail.catalog.product.ws.model.error.APIError;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ResourceMessage {
	
	@JsonProperty("error_mappings")
	private Map<String, String> errorMappings;
	
	@JsonProperty("error_messages")
	private Map<String, APIError> errorMessages;

	public Map<String, String> getErrorMappings() {
		return errorMappings;
	}

	public void setErrorMappings(Map<String, String> errorMappings) {
		this.errorMappings = errorMappings;
	}

	public Map<String, APIError> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(Map<String, APIError> errorMessages) {
		this.errorMessages = errorMessages;
	}

	@Override
	public String toString() {
		return "ResourceMessage [errorMappings=" + errorMappings + ", errorMessages=" + errorMessages + "]";
	}
	
	

}
