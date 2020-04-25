package com.myretail.catalog.product.ws.mapping;

import org.springframework.stereotype.Component;

import com.myretail.catalog.product.ws.dto.ProductDTO;
import com.myretail.catalog.product.ws.model.Product;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component
public class BeanMapper extends ConfigurableMapper	 {
	
	@Override
	    protected void configure(MapperFactory mapperFactory) {
	        mapperFactory.classMap(ProductDTO.class, Product.class)
	                .field("availability.id", "id")
	                .field("item.description.title", "name")
	                .register();
	        
	        
}
	 
}
