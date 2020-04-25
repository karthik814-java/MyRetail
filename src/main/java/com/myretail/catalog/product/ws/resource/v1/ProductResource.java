package com.myretail.catalog.product.ws.resource.v1;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myretail.catalog.product.ws.common.ProductConstants;
import com.myretail.catalog.product.ws.model.Product;
import com.myretail.catalog.product.ws.service.ProductOrchestratorService;
import com.myretail.catalog.product.ws.util.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("myretail/api/v1")
@Api(tags = { "MyRetail - Product API" })
public class ProductResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductResource.class);

	@Autowired
	private ProductOrchestratorService productOrchestrator;

	
	@ApiOperation(value = "Get Product Details", notes = "Get the Product details ( Name & price) by Id", response = Product.class, httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product Details Response", response = Product.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping(value = "products/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Product> getProduct(@PathVariable(value = "id") String productId,
			@RequestHeader(ProductConstants.X_TARGET_CLIENT_ID) String clientId) {

		final String methodName = "[ ProductResource :: getProduct ]";

		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_START_MSG, methodName);

		// validate input parameters
		CommonUtil.validateRequestParams(clientId, productId, null);

		LOGGER.info("Retrieve product details for id :: {}", productId);

		// Get Product details
		Product product = productOrchestrator.getProductInfo(productId);

		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_END_MSG, methodName);

		ResponseEntity<Product> response = CommonUtil.buildSuccessResponse(product);
		
		return response;
	}
	
	

	@ApiOperation(value = "Update Product Details", notes = "Updates the Product details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Updated Product Details Response"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@PutMapping(value = "products/{id}", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") String productId,
			@RequestHeader(ProductConstants.X_TARGET_CLIENT_ID) String clientId, @Valid @RequestBody Product product) {

		final String methodName = "[ ProductResource :: updateProduct ]";

		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_START_MSG, methodName);

		LOGGER.info("Updating product details for id :: {} with this product :: {}", productId, product.toString());

		// validate input parameters
		CommonUtil.validateRequestParams(clientId, productId, product);

		// update the product details
		Product updatedProduct = productOrchestrator.updateProductInfo(product, productId);

		ResponseEntity<Product> response = CommonUtil.buildSuccessResponse(updatedProduct);
		
		LOGGER.debug(ProductConstants.LOG_DEBUG_METHOD_END_MSG, methodName);

		return response;

	}

}
