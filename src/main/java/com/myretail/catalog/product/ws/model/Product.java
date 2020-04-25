package com.myretail.catalog.product.ws.model;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Product implements Serializable {
	
	
	private static final long serialVersionUID = -4113122652143530308L;
	
	@NotNull (message = "Product Id is mandatory in request body")
	@Digits(fraction = 0, integer = 10, message="Please provide valid product id with in 10 digits")
	private Long id;
	private String name;
	
	@JsonProperty("current_price")
	@NotNull
    @Valid
	private Price price;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Price getPrice() {
		return price;
	}
	public void setPrice(Price price) {
		this.price = price;
	}
	
	
	public Product() {
		super();
	}
	public Product(
			@NotNull(message = "Product Id is mandatory in request body") @Digits(fraction = 0, integer = 10, message = "Please provide valid product id with in 10 digits") Long id,
			String name, @NotNull @Valid Price price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
	}
	
	
	
	
	
	

}
