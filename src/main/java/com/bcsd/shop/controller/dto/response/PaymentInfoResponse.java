package com.bcsd.shop.controller.dto.response;

import com.bcsd.shop.domain.Payment;
import com.bcsd.shop.domain.PaymentMethod;
import com.bcsd.shop.domain.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PaymentInfoResponse(
    Long id,
    String transactionId,
    Long amount,
    PaymentMethod method,
    PaymentStatus status,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt
) {
    public static PaymentInfoResponse from(Payment payment) {
        return new PaymentInfoResponse(
                payment.getId(),
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
