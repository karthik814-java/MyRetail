package com.myretail.catalog.product.ws.client;


import java.util.concurrent.TimeUnit;
import  com.myretail.catalog.product.ws.model.error.Error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.myretail.catalog.product.ws.client.response.ProductDetailResponse;
import com.myretail.catalog.product.ws.common.ProductConstants;
import com.myretail.catalog.product.ws.dto.ProductDTO;

@Component
public class ProductDetailServiceClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDetailServiceClient.class);

	@Value("${get.product.redsky.target.host}")
	private String  productDetailsHost;

	@Value("${get.product.redsky.target.basepath}")
	private String  productDetailsBasepath;

	@Value("${get.product.redsky.target.params}")
	private String  productDetailsParams;

	@Autowired
	@Qualifier("getProductRestTemplate")
	private RestTemplate getProductRestTemplate;


	public ProductDetailServiceClient() {

	}
	
	
	/**
	 * This method invokes Redsky API RestFul API.
	 * @param productId
	 * @return ProductDetailResponse
	 */

	public ProductDetailResponse getProductDetails(Long productId) {
		Stopwatch startTime = Stopwatch.createStarted();

		final String methodName = "[ ProductDetailServiceClient :: getProductDetails ]";
		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_START_MSG, methodName);

		ResponseEntity<ProductDTO> response = null;
		final StringBuilder Url = new StringBuilder();
		Url.append(productDetailsHost).append(productDetailsBasepath).append(productId).append("?").append(productDetailsParams);

		try {
			getProductRestTemplate.getMessageConverters().add(0,createMappingJacksonHttpMessageConverter());
			response = getProductRestTemplate.getForEntity(Url.toString(), ProductDTO.class);

			LOGGER.info( "Response Time : " + startTime.elapsed(TimeUnit.MILLISECONDS) + " ms for getProductDetails: ");

		}
		catch(Exception e) {
			LOGGER.error("Exception occured while invoking Redsky API for product name {} : ", e);	
			return responseMapper(new ResponseEntity<ProductDTO>(new ProductDTO(), HttpStatus.INTERNAL_SERVER_ERROR));
		}

		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_END_MSG, methodName);

		return responseMapper(response);
	}



	private MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = null;
		try {
			converter = new MappingJackson2HttpMessageConverter();
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			converter.setObjectMapper(mapper);
		} catch (Exception e) {
			LOGGER.error("Exception occured in deserializing product response {} : ", e);
		}
		return converter;
	}

	/**
	 * This method map response received from External Redsky product API to {@link ProductDetailResponse}.
	 *
	 * @return mapped response
	 */
	private ProductDetailResponse responseMapper(ResponseEntity<ProductDTO> response) {

		final String methodName = "[ ProductDetailServiceClient :: responseMapper ]";
		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_START_MSG, methodName);
		ProductDetailResponse productDetailResponse = null;

		if( null!= response ) {
			HttpStatus respStatus = response.getStatusCode();

			productDetailResponse = new ProductDetailResponse();
			LOGGER.info("---------- Response Status " + respStatus + " - body: " + response.hasBody());

			// Constructing empty product detail object incase of 5xx errors to proceed with just product price information with out name.
			
			if (respStatus.is2xxSuccessful() || respStatus.is5xxServerError()) {
				ProductDTO productDTO;
				if(response.hasBody()) {
					
					productDTO = response.getBody();

				} else {
					LOGGER.error("Product API did not send a body");	
					productDTO = new ProductDTO();
				}
				productDetailResponse.setProductDTO(productDTO);


			} else {
				// Assuming external API returns error in future, though it's not sending now
				productDetailResponse.setError(new Error("201"));
				LOGGER.error("Response failed for Redsky API :" + respStatus.getReasonPhrase() );		
			}
		}
		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_END_MSG, methodName);
		return productDetailResponse;
	}
}
