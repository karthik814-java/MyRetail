package com.myretail.catalog.product.ws.resource.v1;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.myretail.catalog.product.ws.model.Price;
import com.myretail.catalog.product.ws.model.Product;
import com.myretail.catalog.product.ws.service.ProductOrchestratorService;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductResourceTest.class)
public class ProductResourceTest {

	public static final String CLIENT_ID_HEADER = "X-Target-ClientId";

	public static final String CLIENT_ID_HEADER_VALUE = "TARGET";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductOrchestratorService productOrchestrator;


	@Test
	public void testGetProductDetails() throws Exception {

		mockGetService("13860428");
		mockMvc.perform(get("/myretail/api/v1/products/13860428").contentType(MediaType.APPLICATION_JSON)
				.header(CLIENT_ID_HEADER, CLIENT_ID_HEADER_VALUE)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		      	.andExpect(jsonPath("$.current_price.price").value("199.99"));
	}

	@Test
	public void testGetProductDetailsInvalidProduct() throws Exception {
		mockGetService("1386042811");
		mockMvc.perform(get("/myretail/api/v1/products/13860428111").contentType(MediaType.APPLICATION_JSON)
				.header(CLIENT_ID_HEADER, CLIENT_ID_HEADER_VALUE)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testGetProductDetailsInvalidProductWithAlphaNumeric() throws Exception {
		mockGetService("1386042811");
		mockMvc.perform(get("/myretail/api/v1/products/7y7y7u8eu38e3").contentType(MediaType.APPLICATION_JSON)
				.header(CLIENT_ID_HEADER, CLIENT_ID_HEADER_VALUE)).andExpect(status().isBadRequest());
	}

	@Test
	public void testGetProductDetailsInvalidClientID() throws Exception {
		mockGetService("13860428");
		mockMvc.perform(get("/myretail/api/v1/products/13860428").contentType(MediaType.APPLICATION_JSON)
				.header(CLIENT_ID_HEADER, "TARGETTTT")).andExpect(status().isBadRequest());
	}


	@Test
	public void testUpdateProductDetails() throws Exception {

		Product product = mockProduct("13860428");

		when(productOrchestrator.updateProductInfo(Mockito.any(Product.class), Mockito.anyString() ))
		.thenReturn(product);

		mockMvc.perform(put("/myretail/api/v1/products/13860428").contentType(MediaType.APPLICATION_JSON)
				.content(getProductInJson(product.getId()))
				.header(CLIENT_ID_HEADER, CLIENT_ID_HEADER_VALUE)).andExpect(status().isOk());
	}


	private void mockGetService(String productId) {

		Product product = mockProduct(productId);
		when(productOrchestrator.getProductInfo(productId)).thenReturn(product);
	}


	private Product mockProduct(String productId) {

		Product product = new Product(Long.valueOf(productId), "The Big Lebowski (Blu-ray)",
				new Price(Long.valueOf(productId), BigDecimal.valueOf(199.99), "USD"));
		return product;

	}

	private String getProductInJson(long id) {

		return "{\r\n" + "\"id\":" + id + ",\r\n"
				+ "    \"name\": \"The Big Lebowski (Blu-ray)\",\r\n" + "    \"current_price\": {\r\n"
				+ "        \"currencyCode\": \"USD\",\r\n" + "\"price\": 299.99\r\n" + "    }\r\n" + "}";

	}


}
