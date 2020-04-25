package com.myretail.catalog.product.ws.model.error;



import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("error")
@JsonTypeInfo(include= JsonTypeInfo.As.WRAPPER_OBJECT,use= JsonTypeInfo.Id.NAME)

public class Error implements Serializable {

    
    private static final long serialVersionUID = -3194385264209408914L;

    
    private String errorCode;
    
    @JsonIgnore
    private HttpStatus httpStatus;
    
    private List<String> errors;

    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    
    @JsonIgnore
    private String cause;
    
    private String message;
    
    private String developerMessage;
    
    public Error() {
        timestamp = LocalDateTime.now();
    }
    
    public Error(String errorCode) {
        this();
        this.errorCode = errorCode;
    }
    
    
    public Error(HttpStatus httpStatus) {
        this();
        this.httpStatus = httpStatus;
    }

    Error(HttpStatus status, Throwable ex) {
        this();
        this.httpStatus = status;
        this.message = "Unexpected error";
        this.developerMessage = ex.getLocalizedMessage();
    }

    public Error(HttpStatus status, String message, Throwable ex) {
        this();
        this.httpStatus = status;
        this.message = message;
        this.developerMessage = ex.getLocalizedMessage();
    }
    
    public Error(HttpStatus httpStatus, String message, String developerMessage, String error) {
        this();
        this.httpStatus = httpStatus;
        this.message = message;
        this.developerMessage = developerMessage;
        this.errors = Arrays.asList(error);
    }
    
    public Error(HttpStatus httpStatus, String message, String developerMessage, List<String> errors) {
        this();
        this.httpStatus = httpStatus;
        this.message = message;
        this.developerMessage = developerMessage;
        this.errors = errors;
    }
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(HttpStatus status) {
		this.httpStatus = status;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}


	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	@Override
	public String toString() {
		return "Error [errorCode=" + errorCode + ", httpStatus=" + httpStatus + ", errors=" + errors + ", timestamp="
				+ timestamp + ", cause=" + cause + ", message=" + message + ", developerMessage=" + developerMessage
				+ "]";
	}


	
    
}

