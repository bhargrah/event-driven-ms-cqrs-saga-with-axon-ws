package com.bhargrah.products.query;

import java.util.ArrayList;
import java.util.List;

import com.bhargrah.products.core.data.ProductEntity;
import com.bhargrah.products.core.data.ProductsRepository;
import com.bhargrah.products.query.rest.ProductRestModel;
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
	public List<ProductRestModel>findProducts(FindProductsQuery query){
		
		List<ProductRestModel>productsRest = new ArrayList<>();
		
		List<ProductEntity>storedProducts = productsRepository.findAll();
		
		for(ProductEntity productEntity : storedProducts) {
			ProductRestModel productRestModel =  new ProductRestModel();
			BeanUtils.copyProperties(productEntity, productRestModel);
			
			productsRest.add(productRestModel);
			
		}
		
		return productsRest;
	}
	

}
