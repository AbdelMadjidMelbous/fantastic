package com.example.fantastic.controller;

import org.springframework.stereotype.Controller;

import com.example.fantastic.model.Customer;
import com.example.fantastic.model.CustomerOrder;
import com.example.fantastic.model.Product;
import com.example.fantastic.repository.CustomerRepository;
import com.example.fantastic.repository.OrderRepository;
import com.example.fantastic.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by AbdelMadjid on 18/04/2018.
 */
@Controller
public class OrdersController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public String productsList(Model model){
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("orders", orderRepository.findAll());
        return "orders";
    }

    @RequestMapping(value="/createorder", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrder(@RequestParam String firstName, @RequestParam String lastName, @RequestParam(value="productIds[]") Long[] productIds){

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customerRepository.save(customer);
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCustomer(customerRepository.findById(customer.getCustomerId()).get());
        Set<Product> productSet = new HashSet<Product>();
        for (Long productId:productIds){
            productSet.add(productRepository.findById(productId).get());
        }
        customerOrder.setProducts(productSet);
        Double total = 0.0;
        for (Long productId:productIds){
            total = total + (productRepository.findById(productId).get().getProductPrice());
        }
        customerOrder.setTotal(total);
        orderRepository.save(customerOrder);

        return customerOrder.getOrderId().toString();
    }

    @RequestMapping(value = "/removeorder", method = RequestMethod.POST)
    @ResponseBody
    public String removeOrder(@RequestParam Long Id){
        orderRepository.deleteById(Id);
        return Id.toString();
    }


}
