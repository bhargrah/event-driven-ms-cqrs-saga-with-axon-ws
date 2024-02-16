package com.bhargrah.orders.repositories;

import com.bhargrah.orders.repositories.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<ProductEntity, String>{

	ProductEntity findByProductId(String productId);
	
	ProductEntity findByProductIdOrTitle(String productId, String title);
}
