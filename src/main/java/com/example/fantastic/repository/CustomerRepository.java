package com.example.fantastic.repository;

import com.example.fantastic.model.Customer;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
