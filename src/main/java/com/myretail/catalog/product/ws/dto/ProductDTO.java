package com.myretail.catalog.product.ws.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("product")
@Data
public class ProductDTO implements Serializable {
	
	private static final long serialVersionUID = 3939955192463007815L;
	
	
	@JsonProperty("item")
	private ItemDTO item;

	@JsonProperty("available_to_promise_network")
	private AvailabilityDTO availability;
	
	
	public AvailabilityDTO getAvailability() {
		return availability;
	}
	public void setAvaialability(AvailabilityDTO availability) {
		this.availability = availability;
	}
	public void setItem(ItemDTO item) {
		this.item = item;
	}
	public ItemDTO getItem() {
		return item;
	}

}
