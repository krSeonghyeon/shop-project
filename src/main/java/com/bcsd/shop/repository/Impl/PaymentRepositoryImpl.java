package com.bcsd.shop.repository.Impl;

import com.bcsd.shop.domain.Payment;
import com.bcsd.shop.repository.PaymentRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Payment saveAndRefresh(Payment payment) {
        entityManager.persist(payment);
        entityManager.flush();
        entityManager.refresh(payment);
        return payment;
    }
}
