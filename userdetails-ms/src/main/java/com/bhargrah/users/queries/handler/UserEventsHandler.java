package com.bhargrah.users.queries.handler;

import com.bhargrah.model.PaymentDetails;
import com.bhargrah.model.User;
import com.bhargrah.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventsHandler {

    @QueryHandler
    public User handle(FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery){

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("5673547")
                .cvv("456")
                .name("Rahul")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        User user = User.builder()
                .firstName("Rahul")
                .lastName("Bhargava")
                .userId(fetchUserPaymentDetailsQuery.getUserId())
                .paymentDetails(paymentDetails)
                .build();

        return user;

    }

}
