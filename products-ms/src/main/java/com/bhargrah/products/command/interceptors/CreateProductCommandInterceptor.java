package com.bhargrah.products.command.interceptors;

import com.bhargrah.products.command.CreateProductCommand;
import com.bhargrah.products.repositories.ProductLookupRepository;
import com.bhargrah.products.repositories.entity.ProductLookupEntity;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Slf4j
@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
	
	private final ProductLookupRepository productLookupRepository;
	
	@Autowired
	public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}

	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
		
		return (index, command)->{
			
			log.info("Intercepted command: " + command.getPayloadType());
			
			if(CreateProductCommand.class.equals(command.getPayloadType())) {
				
				CreateProductCommand createProductCommand = (CreateProductCommand)command.getPayload();
				
				// Check if the product already exists:
				ProductLookupEntity productLookupEntity = productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(), createProductCommand.getTitle());

				if(productLookupEntity != null) {
						throw new IllegalStateException(
										String.format("Product with productId %s or title %s already exists", 
										createProductCommand.getProductId(), createProductCommand.getTitle())
										);
						}
					
				}
			
			return command;
		};
	}

}
