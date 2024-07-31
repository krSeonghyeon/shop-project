package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.PaymentCreateRequest;
import com.bcsd.shop.controller.dto.request.PaymentModifyStatusRequest;
import com.bcsd.shop.controller.dto.response.PaymentInfoResponse;
import com.bcsd.shop.domain.Payment;
import com.bcsd.shop.domain.PaymentStatus;
import com.bcsd.shop.repository.PaymentRepository;
import com.bcsd.shop.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
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

        // 실제 결제사와 결제 생성 로직이 있다고 가정

        Payment savedPayment = paymentRepository.saveAndRefresh(payment);

        return PaymentInfoResponse.from(savedPayment);
    }

    @Override
    @Transactional
    public PaymentInfoResponse cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 결제입니다."));

        if (payment.getStatus() != PaymentStatus.정상결제) {
            throw new IllegalStateException("이미 취소되거나 취소신청 중인 결제입니다");
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
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 결제입니다."));

        if (payment.getStatus() == request.status()) {
            throw new IllegalStateException("동일한 결제상태로의 변경요청입니다");
        }

        payment.changeStatus(request.status());

        Payment updatedPayment = paymentRepository.saveAndRefresh(payment);

        return PaymentInfoResponse.from(updatedPayment);
    }
}
