package com.bcsd.shop.controller.dto.response;

import com.bcsd.shop.domain.Purchase;
import com.bcsd.shop.domain.PurchaseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PurchaseInfoResponse(
        Long id,
        String productName,
        String userName,
        String sellerName,
        Long paymentId,
        Long price,
        Integer quantity,
        Integer shippingCost,
        Long amount,
        String shippingAddress,
        String request,
        PurchaseStatus status,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {
    public static PurchaseInfoResponse from(Purchase purchase) {
        Long amount = purchase.getPrice() * purchase.getQuantity() + purchase.getShippingCost();

        return new PurchaseInfoResponse(
                purchase.getId(),
                purchase.getProduct().getName(),
                purchase.getUser().getName(),
                purchase.getSeller().getName(),
                purchase.getPayment().getId(),
                purchase.getPrice(),
                purchase.getQuantity(),
                purchase.getShippingCost(),
                amount,
                purchase.getShippingAddress(),
                purchase.getRequest(),
                purchase.getStatus(),
                purchase.getCreatedAt(),
                purchase.getUpdatedAt()
        );
    }
}
