package com.myretail.catalog.product.ws.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.myretail.catalog.product.ws.common.ProductConstants;
import com.myretail.catalog.product.ws.config.MessageResourceConfig;
import com.myretail.catalog.product.ws.model.error.Error;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ProductServiceExceptionHandler extends ResponseEntityExceptionHandler{


	@ExceptionHandler(value = ProductServiceException.class)
	public ResponseEntity<Error> exception(ProductServiceException exception) {
		return buildResponseEntity(exception);
	}


	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return the Error object
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Error> handleConstraintViolation(
			javax.validation.ConstraintViolationException ex) {
		Error error = new Error(BAD_REQUEST);
		error.setMessage("Validation error");
		error.setDeveloperMessage(ex.getConstraintViolations().toString());
		return new ResponseEntity<>(error, error.getHttpStatus());
	}


	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the Error object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, 
			HttpHeaders headers, 
			HttpStatus status, 
			WebRequest request) {
		List<String> errors = new ArrayList<String>();
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getDefaultMessage());
		}

		Error error = 
				new Error(HttpStatus.BAD_REQUEST,"Request Validation Failed ", errors.toString(), errors);
		return new ResponseEntity<>(error, error.getHttpStatus());

	}


	/**
	 * Handle MethodArgumentTypeMismatchException, handle generic MethodArgumentTypeMismatchException.class
	 *
	 * @param ex the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		Error error = new Error(BAD_REQUEST);
		error.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		error.setDeveloperMessage(ex.getMessage());
		return new ResponseEntity<>(error, error.getHttpStatus());
	}


	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex the Exception
	 * @return the ApiError object
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Error> handleCustomAPIException(Exception ex,
			WebRequest request) {
		return buildResponseEntity( new ProductServiceException(ProductConstants.DEFAULT_ERROR_CODE, "Technical Error occured in processing your request"))  ;

	}
	
	

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		Error error = 
				new Error(HttpStatus.BAD_REQUEST,"Malformed Json Request ", ex);
		return new ResponseEntity<>(error, error.getHttpStatus());

	}




	private ResponseEntity<Error> buildResponseEntity(ProductServiceException exception) {

		Error error = MessageResourceConfig.getAPIErrorBinding(exception);
		return new ResponseEntity<>(error, error.getHttpStatus());
	}

}
