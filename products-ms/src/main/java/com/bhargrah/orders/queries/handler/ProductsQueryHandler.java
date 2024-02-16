package com.bhargrah.orders.queries.handler;

import java.util.ArrayList;
import java.util.List;

import com.bhargrah.orders.repositories.entity.ProductEntity;
import com.bhargrah.orders.repositories.ProductsRepository;
import com.bhargrah.orders.queries.model.Product;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductsQueryHandler {
	
	private final ProductsRepository productsRepository;

	@Autowired
	public ProductsQueryHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}
	
	@QueryHandler
	public List<Product>findProducts(FindProductsQuery query){
		
		List<Product>productsRest = new ArrayList<>();
		
		List<ProductEntity>storedProducts = productsRepository.findAll();
		
		for(ProductEntity productEntity : storedProducts) {
			Product productRestModel =  new Product();
			BeanUtils.copyProperties(productEntity, productRestModel);
			productsRest.add(productRestModel);
		}
		
		return productsRest;
	}
	

}
