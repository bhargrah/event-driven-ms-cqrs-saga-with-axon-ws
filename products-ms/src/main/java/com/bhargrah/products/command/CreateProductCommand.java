package com.bhargrah.products.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Builder
@Data
public class CreateProductCommand {

	// This field will associate the command and the aggregate:
	@TargetAggregateIdentifier
	private final String productId;
	
	private final String title;	
	private final BigDecimal price;
	private final Integer quantity;
}
