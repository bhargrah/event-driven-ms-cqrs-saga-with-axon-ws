package com.bhargrah.payments.repositories;

import com.bhargrah.payments.repositories.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<PaymentEntity, String>{

}
