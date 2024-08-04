package com.bcsd.shop.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PurchaseCreateRequest(
        Long productId,
        Long paymentId,

        @NotNull(message = "개수는 비어있을 수 없습니다")
        @Min(value = 1, message = "개수는 1이상이어야 합니다")
        Integer quantity,

        @Size(max = 255, message = "요청사항은 최대 255자까지 가능합니다")
        String request
) {
}
