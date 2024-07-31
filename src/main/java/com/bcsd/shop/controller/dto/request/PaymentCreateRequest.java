package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.domain.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PaymentCreateRequest(
        @NotNull(message = "결제금액은 비어있을 수 없습니다")
        @Min(value = 0, message = "결제금액은 0이상이어야 합니다")
        Long amount,

        @NotNull(message = "결제방법은 비어있을 수 없습니다")
        PaymentMethod method
) {
}
