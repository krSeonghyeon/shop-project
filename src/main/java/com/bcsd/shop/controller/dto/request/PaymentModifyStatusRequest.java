package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.annotation.ValidEnum;
import com.bcsd.shop.domain.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PaymentModifyStatusRequest(
        @Schema(example = "환불완료", description = "새결제상태")
        @NotNull(message = "새 결제상태는 비어있을 수 없습니다")
        @ValidEnum(enumClass = PaymentStatus.class, message = "결제상태는 '정상결제', '취소신청', '환불완료'만 가능합니다.")
        String status
) {
}
