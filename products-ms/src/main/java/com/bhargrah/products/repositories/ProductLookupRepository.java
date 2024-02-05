package com.bhargrah.products.repositories;

import com.bhargrah.products.repositories.entity.ProductLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String>{
	ProductLookupEntity findByProductIdOrTitle(String productId, String title);
}
