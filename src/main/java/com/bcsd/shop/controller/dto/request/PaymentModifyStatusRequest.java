package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.domain.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public record PaymentModifyStatusRequest(
        @NotNull(message = "새 결제상태는 비어있을 수 없습니다")
        PaymentStatus status
) {

}
