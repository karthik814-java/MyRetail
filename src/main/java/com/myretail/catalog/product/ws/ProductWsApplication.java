package com.myretail.catalog.product.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@EnableHystrix
@EnableCircuitBreaker
@EnableHystrixDashboard
@ComponentScan({ "com.myretail.catalog.product.ws.circuit", 
				 "com.myretail.catalog.product.ws.resource.v1",
				 "com.myretail.catalog.product.ws.dao",
				 "com.myretail.catalog.product.ws.client",
				 "com.myretail.catalog.product.ws.repository",
				 "com.myretail.catalog.product.ws.service",
				 "com.myretail.catalog.product.ws.exception",
				 "com.myretail.catalog.product.ws.config",
				 "com.myretail.catalog.product.ws.mapping"})
public class ProductWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductWsApplication.class, args);
	}

}
