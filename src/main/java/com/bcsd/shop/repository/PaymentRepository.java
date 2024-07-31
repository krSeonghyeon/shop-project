package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {
}
