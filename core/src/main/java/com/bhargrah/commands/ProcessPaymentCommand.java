package com.bhargrah.commands;

import com.bhargrah.model.PaymentDetails;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ProcessPaymentCommand {

	@TargetAggregateIdentifier
	private final String paymentId;
	
	private final String orderId;
	
	private final PaymentDetails paymentDetails;
}
