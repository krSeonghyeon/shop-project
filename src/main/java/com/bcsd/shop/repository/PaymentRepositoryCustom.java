package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Payment;

public interface PaymentRepositoryCustom {

    Payment saveAndRefresh(Payment payment);
}
