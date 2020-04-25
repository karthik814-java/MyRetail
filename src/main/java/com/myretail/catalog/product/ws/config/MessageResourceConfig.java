package com.myretail.catalog.product.ws.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.myretail.catalog.product.ws.common.ProductConstants;
import com.myretail.catalog.product.ws.exception.ProductServiceException;
import com.myretail.catalog.product.ws.exception.ResourceMessage;
import com.myretail.catalog.product.ws.model.error.APIError;
import com.myretail.catalog.product.ws.model.error.Error;

@Configuration
public class MessageResourceConfig {
	
	/**
	 * {@code msgMap} store all error mapping.
	 */
	private static ResourceMessage msgMap;
	
	
	

	/**
	 * Initializer method.
	 *
	 * @throws IOException in case of IO Error
	 */
	@PostConstruct
	public void init() throws IOException {

		ClassPathResource resource = new ClassPathResource("product-api-errormappings.yml");

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

		msgMap = mapper.readValue(resource.getInputStream(), ResourceMessage.class);

	}
	
	
	
	/**
	 * Get corresponding API error code for the given external service error code.
	 *
	 * @param serviceErrCode service error code
	 * @return api error code if found otherwise default error code
	 */
	public static String getAPIErrorCode(final String serviceErrCode) {
		return ObjectUtils.defaultIfNull(msgMap.getErrorMappings().get(serviceErrCode), ProductConstants.DEFAULT_ERROR_CODE);
	}

	
	
	/**
	 * Get API Error.
	 *
	 */
	public static Error getAPIErrorBinding(final ProductServiceException exception) {

		String code = ObjectUtils.defaultIfNull(exception.getErrorCode(), ProductConstants.DEFAULT_ERROR_CODE);
		HttpStatus status = null;
		
		APIError apiError = getAPIError(code);
		
		if ( apiError.getHttpcode() == 500 || apiError.getHttpcode() == 0 ) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		} else if ( apiError.getHttpcode() == 400 ) {
			status = HttpStatus.BAD_REQUEST;
		} 
		
		Error errBinding = new Error(status);
		errBinding.setErrorCode(code);
		errBinding.setMessage(apiError.getApimsg());
		errBinding.setDeveloperMessage(exception.getDeveloperMessage());
		return errBinding;
	}
	
	

	public static APIError getAPIError(final String apiErrorCode) {
		return msgMap.getErrorMessages().get(apiErrorCode);
	}

}
