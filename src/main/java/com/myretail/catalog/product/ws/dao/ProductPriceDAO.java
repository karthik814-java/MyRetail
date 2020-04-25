package com.myretail.catalog.product.ws.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.myretail.catalog.product.ws.model.Price;
import com.myretail.catalog.product.ws.repository.ProductPriceRepository;

@Repository
public class ProductPriceDAO {

	@Autowired
	private ProductPriceRepository productPriceRepository;	 

	@Cacheable(value = "productPriceCache", key = "#id")
	public Price getProductPrice(Long id){
		return productPriceRepository.findById(id).orElse(null); 
	}	
	
	
	@CachePut(value = "productPriceCache", key = "#price.id")
	public Price updateProductPrice(Price price){
		return productPriceRepository.save(price); 
	}
}

