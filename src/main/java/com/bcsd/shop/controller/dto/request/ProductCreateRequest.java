package com.bcsd.shop.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(
        @Schema(example = "1", description = "카테고리 번호")
        Long categoryId,

        @Schema(example = "맥북M5", description = "상품명")
        @NotBlank(message = "상품명은 비어있을 수 없습니다")
        @Size(max = 255, message = "상품명은 최대 255자까지 가능합니다")
        String name,

        @Schema(example = "https://example.png", description = "상품 이미지URL")
        @Size(max = 255, message = "이미지url은 최대 255자까지 가능합니다")
        String image,

        @Schema(example = "테스트코드가 매우 빨리돌아갑니다.", description = "상품설명")
        String description,

        @Schema(example = "2000000", description = "가격")
        @NotNull(message = "가격은 비어있을 수 없습니다")
        @Min(value = 0, message = "가격은 0이상이어야 합니다")
        Long price,

        @Schema(example = "3000", description = "배송비")
        @NotNull(message = "배송비는 비어있을 수 없습니다")
        @Min(value = 0, message = "배송비는 0이상이어야 합니다")
        Integer shippingCost,

        @Schema(example = "50", description = "재고 수")
        @NotNull(message = "재고는 비어있을 수 없습니다")
        @Min(value = 0, message = "재고는 0이상이어야 합니다")
        Integer stock
) {
}
