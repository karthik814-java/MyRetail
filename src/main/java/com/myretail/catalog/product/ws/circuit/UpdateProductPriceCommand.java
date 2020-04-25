package com.myretail.catalog.product.ws.circuit;


import com.myretail.catalog.product.ws.dao.ProductPriceDAO;
import com.myretail.catalog.product.ws.model.Price;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class UpdateProductPriceCommand extends HystrixCommand<Price> {
	
	private final ProductPriceDAO productPriceDao;
	
	private final Price price;
	
	public UpdateProductPriceCommand(Price price, ProductPriceDAO productPriceDao) {
        super(HystrixCommandGroupKey.Factory.asKey("UpdateProductPriceCommand"));
        this.price = price;
        this.productPriceDao=productPriceDao; 
    }

	
	@Override
    protected Price run() throws Exception {  
        return productPriceDao.updateProductPrice(price);
    }  


}
