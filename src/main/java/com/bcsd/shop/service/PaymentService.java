package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.PaymentCreateRequest;
import com.bcsd.shop.controller.dto.response.PaymentInfoResponse;

public interface PaymentService {

    PaymentInfoResponse createPayment(PaymentCreateRequest request);
    PaymentInfoResponse cancelPayment(Long id);
}
