package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.domain.PurchaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PurchaseModifyStatusRequest(
        @Schema(example = "배송중", description = "새 주문상태")
        @NotNull(message = "새 주문상태는 비어있을 수 없습니다")
        PurchaseStatus status
) {

}
