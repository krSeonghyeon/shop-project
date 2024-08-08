package com.bcsd.shop.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PurchaseCreateRequest(
        @Schema(example = "1", description = "상품번호")
        Long productId,

        @Schema(example = "1", description = "결제번호")
        Long paymentId,

        @Schema(example = "3", description = "주문개수")
        @NotNull(message = "개수는 비어있을 수 없습니다")
        @Min(value = 1, message = "개수는 1이상이어야 합니다")
        Integer quantity,

        @Schema(example = "안전 배송 부탁드립니다!", description = "요청사항")
        @Size(max = 255, message = "요청사항은 최대 255자까지 가능합니다")
        String request
) {
}
