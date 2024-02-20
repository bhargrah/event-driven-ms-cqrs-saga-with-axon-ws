package com.bhargrah.payments.events.handler;

import com.bhargrah.events.PaymentProcessedEvent;
import com.bhargrah.payments.repositories.PaymentsRepository;
import com.bhargrah.payments.repositories.entity.PaymentEntity;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentsEventsHandler {
	
	private PaymentsRepository paymentsRepository;
	
	@Autowired
	public PaymentsEventsHandler(PaymentsRepository paymentsRepository) {
		this.paymentsRepository = paymentsRepository;
	}

	@EventHandler
	public void on(PaymentProcessedEvent event) throws Exception {
		log.info("PaymentProcessedEvent is called for orderId: " + event.getOrderId());

		PaymentEntity paymentEntity = new PaymentEntity();
		BeanUtils.copyProperties(event, paymentEntity);

		paymentsRepository.save(paymentEntity);
	}

}
