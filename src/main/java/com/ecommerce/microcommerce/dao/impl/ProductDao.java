package com.ecommerce.microcommerce.dao.impl;

import com.ecommerce.microcommerce.dao.IProductDao;
import com.ecommerce.microcommerce.model.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDao implements IProductDao {
    public static List<Product>products=new ArrayList<>();
    static{
        products.add(new Product(1, new String("Ordinateur Portable"), 350, 120));
        products.add(new Product(2, new String("Aspirateur Robot"), 500, 200));
        products.add(new Product(3, new String("Table de ping pong"), 750, 400));
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public List<Product> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Product> findAll(Iterable<Integer> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void delete(Product product) {

    }

    @Override
    public void delete(Iterable<? extends Product> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Product> List<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Product findOne(Integer integer) {
        return null;
    }

    @Override
    public boolean exists(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Product> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<Product> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Product getOne(Integer integer) {
        return null;
    }

    @Override
    public <S extends Product> S findOne(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Product> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Product> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Product> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Product findById(int id) {
        for(Product product : products){
            if(product.getId() == id){
                return product;
            }
        }
        return null;
    }

    @Override
    public List<Product> findByPrixGreaterThan(int prixLimit) {
        return null;
    }

    @Override
    public List<Product> findByNomLike(String recherche) {
        return null;
    }

    @Override
    public List<Product> chercherUnProduitCher(int prix) {
        return null;
    }

    @Override
    public Product save(Product obj) {
        products.add(obj);
        return obj;
    }
}
