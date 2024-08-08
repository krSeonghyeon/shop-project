package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.domain.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PaymentCreateRequest(
        @Schema(example = "100000", description = "결제금액")
        @NotNull(message = "결제금액은 비어있을 수 없습니다")
        @Min(value = 0, message = "결제금액은 0이상이어야 합니다")
        Long amount,

        @Schema(example = "카드", description = "결제방법")
        @NotNull(message = "결제방법은 비어있을 수 없습니다")
        PaymentMethod method
) {
}
