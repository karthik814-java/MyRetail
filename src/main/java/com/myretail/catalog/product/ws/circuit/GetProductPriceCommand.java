package com.myretail.catalog.product.ws.circuit;

import com.myretail.catalog.product.ws.dao.ProductPriceDAO;
import com.myretail.catalog.product.ws.model.Price;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class GetProductPriceCommand extends HystrixCommand<Price> {
	
	private final ProductPriceDAO productPriceDao;
	
	private final Long productId;
	
	public GetProductPriceCommand(Long productId, ProductPriceDAO productPriceDao) {
        super(HystrixCommandGroupKey.Factory.asKey("GetProductPriceCommand"));
        this.productId = productId;
        this.productPriceDao=productPriceDao; 
    }

	
	@Override
    protected Price run() throws Exception {  
        return productPriceDao.getProductPrice(productId);
    }  


}
