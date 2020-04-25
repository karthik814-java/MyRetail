package com.myretail.catalog.product.ws.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.myretail.catalog.product.ws.common.ProductConstants;
import com.myretail.catalog.product.ws.config.MessageResourceConfig;
import com.myretail.catalog.product.ws.exception.ProductServiceException;
import com.myretail.catalog.product.ws.model.Product;
import com.myretail.catalog.product.ws.model.error.APIError;
import com.myretail.catalog.product.ws.model.error.Error;

public class CommonUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

	/**
	 * Method to validate Request params.
	 * 
	 * @param clientId
	 * @param productId
	 * @param product
	 */
	public static void validateRequestParams(String clientId, String productId, Product product) {

		validateProductId(productId);

		if (!("TARGET").equals(clientId)) {
			throwProductServiceAPIException("101");
		} else if (StringUtils.isNotEmpty(productId) && null != product
				&& !Long.valueOf(productId).equals(product.getId())) {
			throwProductServiceAPIException("103");
		}
	}

	/**
	 * Method to validate the productId.
	 *
	 * @param productId
	 */
	private static void validateProductId(final String productId) {

		if (StringUtils.isBlank(productId)) {
			throwProductServiceAPIException("108");
		} else if (productId.length() > 10) {
			throwProductServiceAPIException("102");
		} else if (!isValid(ProductConstants.PRODUCT_ID_REGULAR_EXPRESSION, productId)) {
			throwProductServiceAPIException("109");
		}
	}

	/**
	 * Method to check the regular expression format of given value.
	 *
	 * @param regularExpression regex
	 * @param value             value to check for regex
	 * @return true if input value matches regex
	 */
	static boolean isValid(final String regularExpression, final String value) {
		boolean isValid = false;
		Pattern pattern;

		pattern = Pattern.compile(regularExpression, Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(value);
		if (matcher.matches()) {
			isValid = true;
		}

		return isValid;
	}

	/**
	 * Method to build Response Entity
	 * 
	 * @param <T>
	 * @param entityInstance
	 * @return response
	 */
	public static <T> ResponseEntity<T> buildSuccessResponse(final T entityInstance) {

		ResponseEntity<T> response = ResponseEntity.ok().body(entityInstance);
		if (response.getBody() == null) {
			LOGGER.debug("Product response :: No response entity");
		} else {
			LOGGER.debug("Product response :: {}", response.getBody());
		}
		return response;

	}

	/**
	 * Method to validate error code and throw appropriate exception.
	 *
	 * @param error Error
	 *
	 */
	public static void validateErrors(final Error error) {
		if (null != error) {
			String errorCode = error.getErrorCode();
			throwProductServiceException(errorCode);
		}
	}

	/**
	 * Method to throw {@link ProductServiceException} based on external API error
	 * code.
	 *
	 * @param apiErrorCode api error code value
	 */
	private static void throwProductServiceException(final String errorCode) {
		throw new ProductServiceException(MessageResourceConfig.getAPIErrorCode(errorCode),
				"External service returned the error code : " + errorCode);
	}

	/**
	 * Method to throw {@link ProductServiceException} based on internal validation
	 * errors/ other exceptions.
	 *
	 * @param apiErrorCode api error code value
	 */
	public static void throwProductServiceAPIException(String apiErrorCode) {

		LOGGER.debug("Api error code passed is {}", apiErrorCode);

		apiErrorCode = ObjectUtils.defaultIfNull(apiErrorCode, ProductConstants.DEFAULT_ERROR_CODE);

		APIError apiError = MessageResourceConfig.getAPIError(apiErrorCode);

		throw new ProductServiceException(apiErrorCode, apiError.getApimsg());
	}

}
