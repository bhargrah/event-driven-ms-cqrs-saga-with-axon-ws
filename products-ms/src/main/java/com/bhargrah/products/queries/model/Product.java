package com.bhargrah.products.queries.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Product {

	private String productId;
	private String title;
	private BigDecimal price;
	private Integer quantity;
}
