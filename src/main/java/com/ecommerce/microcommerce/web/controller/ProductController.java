package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.IProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;


@Api( description="API pour les opérations CRUD sur les produits.")

@RestController
public class ProductController {

    @Autowired
    private IProductDao productDao;


    //Récupérer la liste des produits
    @ApiOperation(value = "Liste l'ensemble des produits.")
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)
    public MappingJacksonValue ProductList() {

        Iterable<Product> produits = productDao.findAll();

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");

        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);

        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);

        produitsFiltres.setFilters(listDeNosFiltres);

        return produitsFiltres;
    }


    //Récupérer un produit par son Id
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value = "/Produits/{id}")

    public Product getProduct(@PathVariable int id) {

        Product produit = productDao.findById(id);

        if(produit==null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

        return produit;
    }




    //ajouter un produit
    @ApiOperation(value = "Ajoute un produits.")
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> addProduct(@Valid @RequestBody Product product) {

        if(product.getPrixAchat() == 0){
            throw new ProduitGratuitException("Le produit avec le nom " + product.getNom() + " que vous souhaitez insérer ne peux pas avoir un prix de vente à 0.");
        }
        Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Supprime un produit.")
    @DeleteMapping (value = "/Produits/{id}")
    public void deleteProduct(@PathVariable int id) {

        productDao.delete(id);
    }

    @ApiOperation(value = "Met à jour un produit.")
    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {

        productDao.save(product);
    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product> requestTest(@PathVariable int prix) {

        return productDao.searchExpensiveProduct(prix);
    }

    @ApiOperation(value = "Calcule la marge de revente d'un produit.")
    @GetMapping(value = "/AdminProduits")
    public HashMap<String, Integer> calcProductMargin(){
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for(Product product : productDao.findAll()){
            map.put(product.toString(), product.getPrix() - product.getPrixAchat());
        }
        return map;
    }

    @ApiOperation(value = "Liste les produits tout en effectuant un tri par ordre alphabétique sur leur nom.")
    @GetMapping(value= "/Produits/sort")
    public List<Product> sortProductsByNomAsc(){
        return productDao.findAllByOrderByNomAsc();

    }



}
