package com.bcsd.shop.controller.dto.request;

import com.bcsd.shop.annotation.ValidEnum;
import com.bcsd.shop.domain.PurchaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PurchaseModifyStatusRequest(
        @Schema(example = "배송중", description = "새 주문상태 '결제완료', '배송중', '구매확정', '취소요청', '취소완료', '반품요청', '반품완료', '교환요청', '교환완료'")
        @NotNull(message = "새 주문상태는 비어있을 수 없습니다")
        @ValidEnum(enumClass = PurchaseStatus.class, message = "주문상태는 '결제완료', '배송중', '구매확정', '취소요청', '취소완료', '반품요청', '반품완료', '교환요청', '교환완료'만 가능합니다.")
        String status
) {

}
