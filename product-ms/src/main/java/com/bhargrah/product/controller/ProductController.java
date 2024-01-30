package com.bhargrah.product.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @PostMapping
    public String createProduct(){
        return "HTTP POST Handler";
    }

    @GetMapping
    public  String getProduct(){
        return "HTTP GET Handler";
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
