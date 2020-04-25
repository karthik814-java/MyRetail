package com.myretail.catalog.product.ws.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myretail.catalog.product.ws.circuit.GetProductPriceCommand;
import com.myretail.catalog.product.ws.circuit.ProductDetailCommand;
import com.myretail.catalog.product.ws.circuit.UpdateProductPriceCommand;
import com.myretail.catalog.product.ws.client.ProductDetailServiceClient;
import com.myretail.catalog.product.ws.client.response.ProductDetailResponse;
import com.myretail.catalog.product.ws.common.ProductConstants;
import com.myretail.catalog.product.ws.dao.ProductPriceDAO;
import com.myretail.catalog.product.ws.exception.ProductServiceException;
import com.myretail.catalog.product.ws.mapping.BeanMapper;
import com.myretail.catalog.product.ws.model.Price;
import com.myretail.catalog.product.ws.model.Product;
import com.myretail.catalog.product.ws.service.ProductOrchestratorService;
import com.myretail.catalog.product.ws.util.CommonUtil;

import rx.Observable;

@Component
public class ProductOrchestratorServiceImpl implements ProductOrchestratorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductOrchestratorServiceImpl.class);

	private final ProductDetailServiceClient productDetailServiceClient;

	private final ProductPriceDAO productPriceDao;

	private final BeanMapper beanMapper;

	@Autowired
	public ProductOrchestratorServiceImpl(final ProductDetailServiceClient productDetailServiceClient,
			final BeanMapper beanMapper, final ProductPriceDAO productPriceDao) {
		this.productPriceDao = productPriceDao;
		this.productDetailServiceClient = productDetailServiceClient;
		this.beanMapper = beanMapper;
	}

	public Product getProductInfo(String prodId) {
		final String methodName = "[ ProductOrchestratorServiceImpl :: getProductInfo ]";
		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_START_MSG, methodName);

		Observable<Product> productAggregateObservable;

		try {
			Long productId = Long.valueOf(prodId);

			productAggregateObservable = Observable.zip(new ProductDetailCommand(productId, productDetailServiceClient)
					.toObservable().onErrorReturn(exception -> {
						LOGGER.error("Hystrix error occured in product lookup from Redsky API {}", exception);
						throw new ProductServiceException("104", exception.getMessage());

					}),
					new GetProductPriceCommand(productId, productPriceDao).toObservable().onErrorReturn(exception -> {
						LOGGER.error("Error occured in fetching product price from DB {}", exception);
						throw new ProductServiceException("105", exception.getMessage());
					}), ((productDetailResponse, productPrice) -> getOrchestratedProductDetails(productDetailResponse,
							productPrice, productId)));

		} catch (NumberFormatException e) {
			LOGGER.error("Number format exception in converting prodId  {}", e);
			throw new ProductServiceException(ProductConstants.DEFAULT_ERROR_CODE, e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Exception occured in aggregating product details  {}", e);
			throw new ProductServiceException(ProductConstants.DEFAULT_ERROR_CODE, e.getMessage());
		}

		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_END_MSG, methodName);

		return productAggregateObservable.toBlocking().single();

	}

	public Product updateProductInfo(Product productDetails, String productId) {
		final String methodName = "[ ProductOrchestratorServiceImpl :: updateProductInfo ]";
		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_START_MSG, methodName);

		Price currentPrice = null;
		Price updatedPrice = null;

		try {
			Long prodId = Long.valueOf(productId);

			Observable<Price> currentPriceObs = new GetProductPriceCommand(prodId, productPriceDao).toObservable()
					.onErrorReturn(exception -> {
						LOGGER.error("Hystrix Error occured in fetching product price from DB {}", exception);
						throw new ProductServiceException("105", exception.getMessage());
					});
			currentPrice = currentPriceObs.toBlocking().single();

			if (null != currentPrice) {
				LOGGER.info("Product id {} already exists in DB, updating it ...", productId);
			} else {
				LOGGER.info("Product id {} doesn't exists in DB, creating it ...", productId);
			}
				Price priceUpdateRequest = productDetails.getPrice();
				priceUpdateRequest.setId(prodId);
				Observable<Price> updatedPriceObs = new UpdateProductPriceCommand(priceUpdateRequest, productPriceDao)
						.toObservable().onErrorReturn(exception -> {
							LOGGER.error("Hystrix Error occured in updating product price in DB  {}", exception);
							throw new ProductServiceException("106", exception.getMessage());
						});
				updatedPrice = updatedPriceObs.toBlocking().single();

		} catch (Exception e) {
			LOGGER.error("Exception occured in updating product details  {}", e);
			throw new ProductServiceException(ProductConstants.DEFAULT_ERROR_CODE, e.getMessage());

		}
		productDetails.setPrice(updatedPrice);

		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_END_MSG, methodName);

		return productDetails;
	}

	public Product getOrchestratedProductDetails(ProductDetailResponse productDetailResponse, Price price,
			Long prodId) {

		Product product = null;

		try {
			// validating service errors
			CommonUtil.validateErrors(productDetailResponse.getError());

			if (productDetailResponse.getProductDTO() == null && price == null) {
				LOGGER.error("Product name & price not found for the given product id : {}", prodId);
				return product;
			}

			if (null != productDetailResponse && null != productDetailResponse.getProductDTO()) {
				product = beanMapper.map(productDetailResponse.getProductDTO(), Product.class);
			} else {
				LOGGER.error("Product name not found for the given product id : {}", prodId);
			}

			if (null != price) {
				product.setPrice(price);
			} else {
				LOGGER.error("Product price not found for the given product id : {}", prodId);
			}

		} catch (Exception e) {
			LOGGER.error("Exception occured in orchestrating product details : {}", e);
			throw new ProductServiceException(ProductConstants.DEFAULT_ERROR_CODE, e.getMessage());
		}

		return product;
	}

}
