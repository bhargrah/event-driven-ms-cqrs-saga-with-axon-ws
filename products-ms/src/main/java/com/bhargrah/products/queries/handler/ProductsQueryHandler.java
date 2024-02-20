package com.bhargrah.products.queries.handler;

import com.bhargrah.products.queries.model.Product;
import com.bhargrah.products.repositories.ProductsRepository;
import com.bhargrah.products.repositories.entity.ProductEntity;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
