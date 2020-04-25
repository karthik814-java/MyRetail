package com.myretail.catalog.product.ws.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.myretail.catalog.product.ws.model.Price;
import com.myretail.catalog.product.ws.repository.ProductPriceRepository;

@Configuration
public class ApplicationConfig {
	
	
	@Bean
	CommandLineRunner commandLineRunner(ProductPriceRepository productPriceRepository) {
		return strings -> {
			productPriceRepository.deleteAll();
			productPriceRepository.save(new Price(13860428L, new BigDecimal("13.49"), "USD"));
			productPriceRepository.save(new Price(15117729L, new BigDecimal("99.99"), "USD"));
			productPriceRepository.save(new Price(16483589L, new BigDecimal("149.99"), "USD"));
			productPriceRepository.save(new Price(16696652L, new BigDecimal("129.99"), "USD"));
			productPriceRepository.save(new Price(16752456L, new BigDecimal("239.99"), "USD"));
			productPriceRepository.save(new Price(15643793L, new BigDecimal("339.99"), "USD"));		

		};
	}
	
	
	
	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheCacheManager().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cacheFactoryBean = new EhCacheManagerFactoryBean();
		cacheFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
		cacheFactoryBean.setShared(true);
		return cacheFactoryBean;
	}

}
