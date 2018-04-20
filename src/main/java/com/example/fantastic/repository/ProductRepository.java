package com.example.fantastic.repository;

import com.example.fantastic.model.Product;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
public interface ProductRepository extends CrudRepository<Product, Long> {
}
