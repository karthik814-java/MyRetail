package com.myretail.catalog.product.ws.config;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.myretail.catalog.product.ws.repository.ProductPriceRepository;


@Configuration
public class SpringConfiguration {
	@Bean
	public ProductPriceRepository createRepository() {
		return mock(ProductPriceRepository.class); 
	}	
}

