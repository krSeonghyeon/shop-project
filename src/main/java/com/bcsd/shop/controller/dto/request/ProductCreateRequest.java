package com.bcsd.shop.controller.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(
        Long categoryId,

        @NotBlank(message = "상품명은 비어있을 수 없습니다")
        @Size(max = 255, message = "상품명은 최대 255자까지 가능합니다")
        String name,

        @Size(max = 255, message = "이미지url은 최대 255자까지 가능합니다")
        String image,

        String description,

        @NotNull
        @Min(value = 0, message = "가격은 0이상이어야 합니다")
        Long price,

        @NotNull
        @Min(value = 0, message = "배송비는 0이상이어야 합니다")
        Integer shippingCost,

        @NotNull
        @Min(value = 0, message = "재고는 0이상이어야 합니다")
        Integer stock
) {
}
