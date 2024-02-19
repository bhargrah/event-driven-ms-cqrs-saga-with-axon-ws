package com.bhargrah.products.queries.controller;

import java.util.List;

import com.bhargrah.products.queries.model.Product;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhargrah.products.queries.handler.FindProductsQuery;

@RestController
@RequestMapping("/products")
public class ProductsQueryController {

	QueryGateway queryGateway;

	@Autowired
	public ProductsQueryController(QueryGateway queryGateway) {
		this.queryGateway = queryGateway;
	}
	
	@GetMapping
	public List<Product>getProducts(){
		
		FindProductsQuery findProductsQuery = new FindProductsQuery();
		List<Product>products = queryGateway.query(findProductsQuery, ResponseTypes.multipleInstancesOf(Product.class)).join();
		return products;
		
	}
}
