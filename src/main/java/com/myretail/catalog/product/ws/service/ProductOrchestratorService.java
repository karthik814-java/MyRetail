package com.myretail.catalog.product.ws.service;

import com.myretail.catalog.product.ws.model.Product;

public interface ProductOrchestratorService {

	Product getProductInfo(String prodId);
	
	Product updateProductInfo(Product product, String prodId);

}
