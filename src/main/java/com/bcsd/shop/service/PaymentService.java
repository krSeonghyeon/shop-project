package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.PaymentCreateRequest;
import com.bcsd.shop.controller.dto.request.PaymentModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PaymentInfoResponse;

public interface PaymentService {

    PaymentInfoResponse getPaymentInfo(Long id);
    PaymentInfoResponse createPayment(PaymentCreateRequest request);
    PaymentInfoResponse cancelPayment(Long id);
    PaymentInfoResponse modifyStatusPayment(Long id, PaymentModifyStatusRequest request);
}
