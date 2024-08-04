package com.bcsd.shop.exception.errorcode;

import com.bcsd.shop.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PurchaseErrorCode implements ErrorCode {

    PURCHASE_NOT_FOUND("404_PURCHASE_NOT_FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    INVALID_OVER_STOCK("400_INVALID_OVER_STOCK", HttpStatus.BAD_REQUEST, "주문개수가 재고보다 큽니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
