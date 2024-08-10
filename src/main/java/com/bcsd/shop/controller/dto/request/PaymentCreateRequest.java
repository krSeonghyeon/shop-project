package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.annotation.ValidEnum;
import com.bcsd.shop.domain.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PaymentCreateRequest(
        @NotBlank(message = "트랜잭션ID는 비어있을 수 없습니다")
        @Size(max = 50, message = "트랜잭션ID는 50자이하여야 합니다")
        String transactionId,

        @Schema(example = "100000", description = "결제금액")
        @NotNull(message = "결제금액은 비어있을 수 없습니다")
        @Min(value = 0, message = "결제금액은 0이상이어야 합니다")
        Long amount,

        @Schema(example = "카드", description = "결제방법")
        @NotNull(message = "결제방법은 비어있을 수 없습니다")
        @ValidEnum(enumClass = PaymentMethod.class, message = "결제방법은 '카드', '계좌이체'만 가능합니다.")
        String method
) {
}
