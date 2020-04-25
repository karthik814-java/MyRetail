package com.myretail.catalog.product.ws.circuit;

import com.myretail.catalog.product.ws.client.ProductDetailServiceClient;
import com.myretail.catalog.product.ws.client.response.ProductDetailResponse;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class ProductDetailCommand extends HystrixCommand<ProductDetailResponse> {
	
	
	private final ProductDetailServiceClient productDetailServiceClient;
	
	private final Long productId;

	 public ProductDetailCommand(Long productId, ProductDetailServiceClient productDetailServiceClient) {
	        super(HystrixCommandGroupKey.Factory.asKey("ProductDetailCommand"));
	        this.productId = productId;
	        this.productDetailServiceClient=productDetailServiceClient; 
	    }

	
	 @Override
	    protected ProductDetailResponse run() throws Exception {  
	        return productDetailServiceClient.getProductDetails(productId);
	    }   
	    
}
