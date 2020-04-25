package com.myretail.catalog.product.ws.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection="price")
public class Price implements Serializable {
	
	
	private static final long serialVersionUID = -8049815377484401840L;


	@Id
	@JsonProperty("id")
	@JsonIgnore
	private Long id;

	
	@NotBlank (message = "Please provide valid currency code")
	@Size(min=2, max=3, message="CurrencyCode should have minimum 2 characters and maximum 3 characters")
	@Pattern(regexp = "^[A-Za-z]+$", message="Please provide valid currency code format")
	private String currencyCode;

	
	@NotNull(message = "Please provide valid price")
    @DecimalMin("0.00")
	@Digits(fraction = 3, integer = 3, message="Please provide price in valid format")
	private BigDecimal price;	
	
	
	
	public Price(){ 
	}

	public Price(Long id, BigDecimal price, String currencyCode) {
		super();
		this.id = id;
		this.price = price;
		this.currencyCode = currencyCode;
	}

	@JsonIgnore
	@JsonProperty(value = "id")
	public Long getId() {
		return id;
	}

	@JsonIgnore
	@JsonProperty(value = "id")
	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Price [id=" + id + ", currencyCode=" + currencyCode + ", price=" + price + "]";
	}

	
	

}
