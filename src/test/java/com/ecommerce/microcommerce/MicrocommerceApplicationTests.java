package com.ecommerce.microcommerce;

import com.ecommerce.microcommerce.dao.IProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.controller.ProductController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MicrocommerceApplicationTests {

	@Autowired
	private ProductController controller;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IProductDao productDao;

	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	public void getProductTest() throws Exception{
		given(productDao.findById(1)).willReturn(new Product(1,"test",100,50));

		mockMvc.perform(get("/Produits/1")
		.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nom", is("test")));
	}

	@Test
	public void getProductsTest() throws Exception{
		List<Product> listTest = new ArrayList<Product>();
		listTest.add(new Product(1, "test1", 100, 20));
		listTest.add(new Product(2, "test2", 250, 40));

		given(productDao.findAll()).willReturn(listTest);

		mockMvc.perform(get("/Produits")
		.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].id", is(listTest.get(0).getId())))
			.andExpect(jsonPath("$[0].nom", is(listTest.get(0).getNom())))
			.andExpect(jsonPath("$[1].id", is(listTest.get(1).getId())))
			.andExpect(jsonPath("$[1].nom", is(listTest.get(1).getNom())));
	}

	@Test
	public void deleteProductTest() throws Exception{
		mockMvc.perform(delete("/Produits/1")
		.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void addProductTest() throws Exception{
		mockMvc.perform(put("/Produits")
		.contentType(MediaType.APPLICATION_JSON)
		.characterEncoding("UTF-8")
		.content("{\"id\":\" 1 \", \"nom\":\"test\",\"prix\": 50, \"prixAchat\": 20}"))
				.andExpect(status().isOk());
	}

	@Test
	public void searchExpensiveProductTest() throws Exception{

		List<Product> listTest = new ArrayList<Product>();
		listTest.add(new Product(1, "test1", 250, 20));
		listTest.add(new Product(2, "test2", 400, 40));

		given(productDao.searchExpensiveProduct(250)).willReturn(listTest);

		List<Product> products = productDao.searchExpensiveProduct(250);

		assertThat(products.size()).isEqualTo(2);
		assertThat(products.size()).isNotNull();
	}

	@Test
	public void findAllByOrderByNomAsc() throws Exception{
		List<Product> listTest = new ArrayList<Product>();
		listTest.add(new Product(1, "arbre", 250, 20));
		listTest.add(new Product(2, "ordinateur", 400, 40));

		given(productDao.findAllByOrderByNomAsc()).willReturn(listTest);

		List<Product> products = productDao.findAllByOrderByNomAsc();

		assertThat(products.get(0).getNom()).isEqualTo("arbre");
	}
}
