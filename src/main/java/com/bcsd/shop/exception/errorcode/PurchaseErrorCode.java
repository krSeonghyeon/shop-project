package com.bcsd.shop.exception.errorcode;

import com.bcsd.shop.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PurchaseErrorCode implements ErrorCode {

    PURCHASE_NOT_FOUND("404_PURCHASE_NOT_FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    PURCHASE_DUPLICATED("409_PURCHASE_DUPLICATED", HttpStatus.CONFLICT, "중복 주문 시도입니다."),
    INVALID_OVER_STOCK("400_INVALID_OVER_STOCK", HttpStatus.BAD_REQUEST, "주문개수가 재고보다 큽니다."),
    INVALID_SAME_PURCHASE_STATUS("400_INVALID_SAME_PURCHASE_STATUS", HttpStatus.BAD_REQUEST, "동일한 주문상태로의 변경요청입니다."),
    INVALID_PURCHASE_CANCEL("400_INVALID_PURCHASE_CANCEL", HttpStatus.BAD_REQUEST, "취소할 수 없는 상태의 주문입니다."),
    INVALID_PURCHASE_STATUS("400_INVALID_PURCHASE_STATUS", HttpStatus.BAD_REQUEST, "올바르지 않은 상태로의 변경요청입니다."),
    INVALID_PURCHASE_DETERMINE("400_INVALID_PURCHASE_DETERMINE", HttpStatus.BAD_REQUEST, "구매 결정이 불가능한 상태입니다."),
    FAILED_PURCHASE("500_FAILED_PURCHASE", HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 에러로 주문에 실패했습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
