package com.myretail.catalog.product.ws.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {

	
	@Bean(name = "getProductRestTemplate")
	public RestTemplate restTemplateForGetProducts(
			@Value("${get.product.read.timeout}") long readTimeout,
			@Value("${get.product.connection.timeout}") long connectTimeout) {
		return generateRestTemplate(readTimeout, connectTimeout);
	}

	
	private RestTemplate generateRestTemplate(long readTimeout, long connectTimeout) {
		return new RestTemplateBuilder().requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
				.setConnectTimeout(Duration.ofMillis(connectTimeout)).setReadTimeout(Duration.ofMillis(readTimeout))
				.build();
	}

}
