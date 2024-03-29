package com.bhargrah.products.command.controller;

import com.bhargrah.products.command.CreateProductCommand;
import com.bhargrah.products.command.model.CreateProduct;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping("/products")
public class ProductsCommandController {

	private final CommandGateway commandGateway;
	
	@Autowired
	public ProductsCommandController(CommandGateway commandGateway) {
		this.commandGateway = commandGateway;
	}

	@PostMapping
	public String createProduct(@Valid @RequestBody CreateProduct createProductRestModel) {
		
		CreateProductCommand createProductCommand =
				CreateProductCommand.builder()
				.title(createProductRestModel.getTitle())
				.price(createProductRestModel.getPrice())
				.quantity(createProductRestModel.getQuantity())
				.productId(UUID.randomUUID().toString())
				.build();
		
		// Send the command to the CommandBus
		String returnValue = commandGateway.sendAndWait(createProductCommand);
		
		// Instead of the try-catch block, Error Handling will be managed by the centralized error handling class:
		/*
		 
		String returnValue = null;
		try {
			returnValue = commandGateway.sendAndWait(createProductCommand);
		} catch (Exception e) {
			returnValue = e.getLocalizedMessage();
		}
		*/
		
		return returnValue;
	}

}
