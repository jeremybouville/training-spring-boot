package com.ecommerce.microcommerce;

import com.ecommerce.microcommerce.dao.IProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.controller.ProductController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ProductController.class, secure = false)
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductDao productDao;

    Product mockProduct = new Product(1, "cookie", 50, 12);
    List<Product> mockProduct2 = new ArrayList<Product>();

    @Test
    public void afficherUnProduitTest() throws Exception{
        Mockito.when(productDao.findById(Mockito.anyInt())).thenReturn(mockProduct);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/Produits/1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "";
        // "{id:1,nom:cookie,prix:10,prixAchat:12}"
        String var = result.getResponse().getContentAsString();

       // JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    public void testedeRequetesTest() throws Exception{
        mockProduct2.add(new Product(1, "gateau", 100, 40));
        mockProduct2.add(new Product(2, "tele", 300, 50));

        Mockito.when(productDao.chercherUnProduitCher(Mockito.anyInt())).thenReturn(mockProduct2);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/test/produits/80").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{id:1,nom:gateau,prix:100,prixAchat:40},{id:2,nom:tele,prix:300,prixAchat:50}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    public void calculerMargeProduitTest() throws Exception{
        mockProduct2.add(new Product(1, "gateau", 100, 40));
        mockProduct2.add(new Product(2, "tele", 300, 50));

        Mockito.when(productDao.chercherUnProduitCher(Mockito.anyInt())).thenReturn(mockProduct2);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/AdminProduits").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{id:1,nom:gateau,prix:100,prixAchat:40}:60,{id:2,nom:tele,prix:300,prixAchat:50}:250";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    public void findByIdTest() throws Exception{
        Product product = productDao.findById(1);

        assertThat(product.getPrix()).isEqualTo(350);
    }

    public void chercherUnProduitCherTest() throws Exception{
        List<Product> products = productDao.chercherUnProduitCher(250);

        assertThat(products.size()).isEqualTo(1);
        assertThat(products.size()).isNotNull();
    }

    public void findAllByOrderByNomAsc() throws Exception{
        List<Product> products = productDao.findAllByOrderByNomAsc();

        assertThat(products.get(1).getNom()).isEqualTo("Ordinateur portable");
    }
}
