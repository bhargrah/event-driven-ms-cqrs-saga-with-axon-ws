package com.bhargrah.products.repositories.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name="products")
public class ProductEntity implements Serializable{


	private static final long serialVersionUID = 5059324003983825666L;

	@Id
	@Column(unique = true)
	private String productId;
	
	@Column(unique=true)
	private String title;
	
	private BigDecimal price;
	
	private Integer quantity;
}
