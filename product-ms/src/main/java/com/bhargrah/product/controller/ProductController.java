package com.bhargrah.product.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private Environment environment;

    @PostMapping
    public String createProduct(){

        return "HTTP POST Handler";
    }

    @GetMapping
    public  String getProduct(){
        return "HTTP GET Handler : " + environment.getProperty("local.server.port");
    }

    @PutMapping
    public  String updateProduct(){
        return "HTTP PUT Handler";
    }

    @DeleteMapping
    public  String deleteProduct(){
        return "HTTP PUT Handler";
    }

}
