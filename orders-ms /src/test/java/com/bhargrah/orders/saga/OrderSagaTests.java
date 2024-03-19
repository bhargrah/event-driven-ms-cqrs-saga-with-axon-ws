package com.bhargrah.orders.saga;

import com.bhargrah.commands.ProcessPaymentCommand;
import com.bhargrah.commands.ReserveProductCommand;
import com.bhargrah.events.ProductReservedEvent;
import com.bhargrah.model.OrderStatus;
import com.bhargrah.model.PaymentDetails;
import com.bhargrah.model.User;
import com.bhargrah.orders.events.OrderCreatedEvent;
import com.bhargrah.query.FetchUserPaymentDetailsQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.test.saga.FixtureConfiguration;
import org.axonframework.test.saga.SagaTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@DisplayName("Order Saga Tests")
public class OrderSagaTests {

    private static final String ORDER_ID = "order_1";
    private static final String PRODUCT_ID = "product_1";
    private static final String USER_ID = "user_1";
    private static final String PAYMENT_ID = "payment_1";
    private static final int QUANTITY = 1;
    private static final String ADDRESS_ID = "address_1";

    private FixtureConfiguration fixture;

    @Mock
    private CommandGateway commandGateway;

    @Mock
    private QueryGateway queryGateway;

    @Mock
    private DeadlineManager deadlineManager;

    @Mock
    private QueryUpdateEmitter queryUpdateEmitter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fixture = new SagaTestFixture<>(OrderSaga.class);
        fixture.registerResource(commandGateway);
        fixture.registerResource(queryGateway);
        fixture.registerResource(deadlineManager);
        fixture.registerResource(queryUpdateEmitter);
    }

    @DisplayName("Should start when Order is created (OrderCreatedEvent)")
    @Test
    void shouldStartWhenOrderIsCreated() {
        fixture.givenNoPriorActivity()
                .whenPublishingA(getOrderCreatedEvent())
                .expectActiveSagas(1);
    }

    @DisplayName("Should handle (OrderCreatedEvent) and emit (ReserveProductCommand)")
    @Test
    void shouldHandleOrderCreatedEventAndEmitReserveProductCommand() {
        fixture.givenNoPriorActivity()
                .whenPublishingA(getOrderCreatedEvent())
                .expectActiveSagas(1)
                .expectDispatchedCommands(getReserveProductCommand());
    }

    // TODO : To be fixed
    @DisplayName("Should handle ProductReservedEvent and emit ProcessPaymentCommand on success")
    @Test
    void shouldHandleProductReservedEventAndEmitProcessPaymentCommandOnSuccess() {

        when(queryGateway.query(any(FetchUserPaymentDetailsQuery.class), (ResponseType<Object>) any()))
                .thenReturn(CompletableFuture.completedFuture(getUser(getPaymentDetails())));

        fixture.givenAPublished(getOrderCreatedEvent())
                .whenPublishingA(getProductReservedEvent())
                .expectActiveSagas(1)
                .expectDispatchedCommands(getProcessPaymentCommand());
    }

    private ProcessPaymentCommand getProcessPaymentCommand() {


        return ProcessPaymentCommand.builder()
                .orderId(ORDER_ID)
                .paymentDetails(getPaymentDetails())
                .paymentId(UUID.randomUUID().toString())
                .build();
    }

    private User getUser(PaymentDetails paymentDetails) {
        return User.builder()
                .firstName("Rahul")
                .lastName("Bhargava")
                .userId(USER_ID)
                .paymentDetails(paymentDetails)
                .build();
    }

    private PaymentDetails getPaymentDetails() {
        return PaymentDetails.builder()
                .cardNumber("5673547")
                .cvv("456")
                .name("Rahul")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();
    }

    private ProductReservedEvent getProductReservedEvent() {
        return ProductReservedEvent.builder()
                .productId(PRODUCT_ID)
                .orderId(ORDER_ID)
                .quantity(QUANTITY)
                .userId(USER_ID)
                .build();
    }

    private OrderCreatedEvent getOrderCreatedEvent() {
        return new OrderCreatedEvent(ORDER_ID, PRODUCT_ID, USER_ID, QUANTITY, ADDRESS_ID, OrderStatus.CREATED);
    }

    private ReserveProductCommand getReserveProductCommand() {
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .productId(PRODUCT_ID)
                .orderId(ORDER_ID)
                .quantity(QUANTITY)
                .userId(USER_ID)
                .build();
        return reserveProductCommand;
    }

}
