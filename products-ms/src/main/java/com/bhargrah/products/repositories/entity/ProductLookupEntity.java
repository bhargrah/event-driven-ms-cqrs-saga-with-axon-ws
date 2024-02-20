package com.bhargrah.products.repositories.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productlookup")
public class ProductLookupEntity implements Serializable {

	private static final long serialVersionUID = -4454392091966382789L;
	
	@Id
	private String productId;
	
	@Column(unique = true)
	private String title;

}
