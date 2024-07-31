package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.PaymentCreateRequest;
import com.bcsd.shop.controller.dto.response.PaymentInfoResponse;
import com.bcsd.shop.domain.Payment;
import com.bcsd.shop.repository.PaymentRepository;
import com.bcsd.shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public PaymentInfoResponse createPayment(PaymentCreateRequest request) {
        Payment payment = Payment.builder()
                .amount(request.amount())
                .method(request.method())
                .build();

        Payment savedPayment = paymentRepository.saveAndRefresh(payment);

        return PaymentInfoResponse.from(savedPayment);
    }
}
