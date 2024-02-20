package com.bhargrah.products.queries.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {

	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
