package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.domain.PurchaseStatus;
import jakarta.validation.constraints.NotNull;

public record PurchaseModifyStatusRequest(
        @NotNull(message = "새 주문상태는 비어있을 수 없습니다")
        PurchaseStatus status
) {

}
