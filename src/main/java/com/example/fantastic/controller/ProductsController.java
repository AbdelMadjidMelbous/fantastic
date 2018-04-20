package com.example.fantastic.controller;

import org.springframework.stereotype.Controller;
import com.example.fantastic.model.Product;
import com.example.fantastic.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by AbdelMadjid on 18/04/2018.
 */
@Controller
public class ProductsController {
    @Autowired
    ProductRepository productRepository;


    @RequestMapping("/product/{id}")
    public String product(@PathVariable Long id, Model model){
        model.addAttribute("product", productRepository.findById(id));
        return "product";
    }

    @RequestMapping(value = "/products",method = RequestMethod.GET)
    public String productsList(Model model){
        model.addAttribute("products", productRepository.findAll());
        return "products";
    }

    @RequestMapping(value = "/saveproduct", method = RequestMethod.POST)
    @ResponseBody
    public String saveProduct(@RequestBody Product product) {
        productRepository.save(product);
        return product.getProductId().toString();
    }

}
