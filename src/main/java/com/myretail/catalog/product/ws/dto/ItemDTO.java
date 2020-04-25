package com.myretail.catalog.product.ws.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ItemDTO {
	
	@JsonProperty("product_description")
	private ProductDescriptionDTO description;

	public ProductDescriptionDTO getDescription() {
		return description;
	}

	public void setDescription(ProductDescriptionDTO description) {
		this.description = description;
	}

	
	

}
