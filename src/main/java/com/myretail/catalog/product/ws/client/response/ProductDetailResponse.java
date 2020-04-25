package com.myretail.catalog.product.ws.client.response;

import com.myretail.catalog.product.ws.dto.ProductDTO;
import com.myretail.catalog.product.ws.model.error.Error;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductDetailResponse {
	
	@Getter @Setter
	private ProductDTO productDTO;
	
	@Getter @Setter
	private Error error;

	public ProductDTO getProductDTO() {
		return productDTO;
	}

	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	

}
