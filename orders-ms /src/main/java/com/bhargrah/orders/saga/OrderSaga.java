package com.bhargrah.orders.saga;

import com.bhargrah.commands.CancelProductReservationCommand;
import com.bhargrah.commands.ProcessPaymentCommand;
import com.bhargrah.commands.ReserveProductCommand;
import com.bhargrah.events.OrderRejectEvent;
import com.bhargrah.events.PaymentProcessedEvent;
import com.bhargrah.events.ProductReservationCancelledEvent;
import com.bhargrah.events.ProductReservedEvent;
import com.bhargrah.model.User;
import com.bhargrah.orders.command.ApproveOrderCommand;
import com.bhargrah.orders.command.RejectOrderCommand;
import com.bhargrah.orders.events.OrderApprovedEvent;
import com.bhargrah.orders.events.OrderCreatedEvent;
import com.bhargrah.query.FetchUserPaymentDetailsQuery;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    private static final Logger log = LoggerFactory.getLogger(OrderSaga.class);

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent){

        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .productId(orderCreatedEvent.getProductId())
                .orderId(orderCreatedEvent.getOrderId())
                .quantity(orderCreatedEvent.getQuantity())
                .userId(orderCreatedEvent.getUserId())
                .build();

        log.info("OrderCreatedEvent handled for orderId : " + orderCreatedEvent.getOrderId() + "and productId : " + orderCreatedEvent.getProductId());

        commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {
            @Override
            public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
                if(commandResultMessage.isExceptional()){
                    //TODO: Start compensating transaction
                }
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent){
        log.info("ProductReservedEvent handled for orderId : " + productReservedEvent.getOrderId() + "and productId : " + productReservedEvent.getProductId());

        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());

        User userPaymentDetails = null;
        try {
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        }catch (Exception ex) {
            log.error(ex.getMessage());
            cancelProductReservation(productReservedEvent,ex.getMessage());
            return;
        }

        if(null == userPaymentDetails) {
            // TODO : Start compensating transactions
            cancelProductReservation(productReservedEvent,"Could not fetch payment details");
            return;
        }

        log.info("Successfully fetched user payment details for user " + userPaymentDetails.getFirstName());

        ProcessPaymentCommand processPaymentCommand = ProcessPaymentCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .paymentId(UUID.randomUUID().toString())
                .build();

        String result = null;
        try {
            result = commandGateway.sendAndWait(processPaymentCommand , 10 , TimeUnit.SECONDS);
        } catch (Exception ex) {
           log.error(ex.getMessage());
           //Start compensation transaction
            cancelProductReservation(productReservedEvent,ex.getMessage());
        }

        if (null == result) log.info("The ProcessPaymentCommand result is NULL. Initiating a compensating transaction");
    }

    private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {

        CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
                .orderId(productReservedEvent.getOrderId())
                .productId(productReservedEvent.getProductId())
                .quantity(productReservedEvent.getQuantity())
                .userId(productReservedEvent.getUserId())
                .reason(reason)
                .build();

        commandGateway.send(cancelProductReservationCommand);

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        ApproveOrderCommand approveOrderCommand =  new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
        commandGateway.send(approveOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent) {
        log.info("Order is approved.Order Saga is complete for orderId: "+ orderApprovedEvent.getOrderId());
        //SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent productReservationCancelledEvent){
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelledEvent.getOrderId(),productReservationCancelledEvent.getReason());
        commandGateway.send(rejectOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectEvent orderRejectEvent){
        log.info("Successfully rejected order with id : " + orderRejectEvent.getOrderId());
    }

}
