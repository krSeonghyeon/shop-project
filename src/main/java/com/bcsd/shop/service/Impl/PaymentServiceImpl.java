package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.PaymentCreateRequest;
import com.bcsd.shop.controller.dto.request.PaymentModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PaymentInfoResponse;
import com.bcsd.shop.domain.Payment;
import com.bcsd.shop.domain.PaymentStatus;
import com.bcsd.shop.exception.CustomException;
import com.bcsd.shop.repository.PaymentRepository;
import com.bcsd.shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bcsd.shop.exception.errorcode.PaymentErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentInfoResponse getPaymentInfo(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(PAYMENT_NOT_FOUND));

        return PaymentInfoResponse.from(payment);
    }

    @Override
    @Transactional
    public PaymentInfoResponse createPayment(PaymentCreateRequest request) {
        Payment payment = Payment.builder()
                .amount(request.amount())
                .method(request.method())
                .build();

        // 실제 결제사와 결제 생성 로직이 있다고 가정

        Payment savedPayment = paymentRepository.saveAndRefresh(payment);

        return PaymentInfoResponse.from(savedPayment);
    }

    @Override
    @Transactional
    public PaymentInfoResponse cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(PAYMENT_NOT_FOUND));

        if (payment.getStatus() != PaymentStatus.정상결제) {
            throw new CustomException(INVALID_ALREADY_CANCELED);
        }

        // 실제 결제사와 결제 취소 로직이 있다고 가정

        payment.changeStatus(PaymentStatus.취소신청);

        Payment updatedPayment = paymentRepository.saveAndRefresh(payment);

        return PaymentInfoResponse.from(updatedPayment);
    }

    @Override
    @Transactional
    public PaymentInfoResponse modifyStatusPayment(Long id, PaymentModifyStatusRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(PAYMENT_NOT_FOUND));

        if (payment.getStatus() == request.status()) {
            throw new CustomException(INVALID_SAME_PAYMENT_STATUS);
        }

        payment.changeStatus(request.status());

        Payment updatedPayment = paymentRepository.saveAndRefresh(payment);

        return PaymentInfoResponse.from(updatedPayment);
    }
}
