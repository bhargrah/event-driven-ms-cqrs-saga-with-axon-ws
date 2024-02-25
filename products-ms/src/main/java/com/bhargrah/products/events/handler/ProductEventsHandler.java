package com.bhargrah.products.events.handler;

import com.bhargrah.events.ProductReservationCancelledEvent;
import com.bhargrah.events.ProductReservedEvent;
import com.bhargrah.products.events.ProductCreatedEvent;
import com.bhargrah.products.repositories.ProductsRepository;
import com.bhargrah.products.repositories.entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {
	
	private ProductsRepository productsRepository;
	
	@Autowired
	public ProductEventsHandler(ProductsRepository productsRepository) {
		this.productsRepository = productsRepository;
	}
	
	//General Exception handling
	@ExceptionHandler(resultType=Exception.class)
	public void handle(Exception exception) throws Exception {
		throw exception;
	}

	@ExceptionHandler(resultType=IllegalArgumentException.class)
	public void handle(IllegalArgumentException exception) throws Exception {
		throw exception;
	}

	@EventHandler
	public void on(ProductCreatedEvent event) throws Exception {
		
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(event, productEntity);
		
		try {
			productsRepository.save(productEntity);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		//Transaction will be rolled back:
		
//		if(true) {
//			throw new Exception("Forcing exception in the Event Handler class");
//		}
		
	}
	
	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) {
		ProductEntity productEntity = productsRepository.findByProductId(productReservedEvent.getProductId());
		log.debug("ProductReservedEvent: Current product quantity: " + productEntity.getQuantity());
		productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
		productsRepository.save(productEntity);
		log.debug("ProductReservedEvent: New product quantity: " + productEntity.getQuantity());
		log.info("productReservedEvent is called for productId: " + productReservedEvent.getProductId() + " and orderId: " + productReservedEvent.getOrderId());
	}
	
	@EventHandler
	public void on(ProductReservationCancelledEvent  productReservationCancelledEvent) {
		ProductEntity currentlyStoredProduct = productsRepository.findByProductId(productReservationCancelledEvent.getProductId());
		log.debug("ProductReservationCancelledEvent: Current product quantity: " + currentlyStoredProduct.getQuantity());
		int newQuantity = productReservationCancelledEvent.getQuantity() + currentlyStoredProduct.getQuantity();
		currentlyStoredProduct.setQuantity(newQuantity);
		productsRepository.save(currentlyStoredProduct);
		log.debug("ProductReservationCancelledEvent: New product quantity: " + currentlyStoredProduct.getQuantity());
	}

	// Help in clearing data from database before replaying events
	@ResetHandler
	public void reset() {
		productsRepository.deleteAll();
	}
}
