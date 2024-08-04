package com.bcsd.shop.exception.errorcode;

import com.bcsd.shop.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

    PAYMENT_NOT_FOUND("404_PAYMENT_NOT_FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 결제입니다."),
    INVALID_PRICE_RANGE("400_INVALID_PRICE_RANGE", HttpStatus.BAD_REQUEST, "최소 가격은 최대 가격보다 클 수 없습니다."),
    INVALID_SAME_PAYMENT_STATUS("400_INVALID_SAME_PAYMENT_STATUS", HttpStatus.BAD_REQUEST, "동일한 결제상태로의 변경요청입니다."),
    INVALID_ALREADY_CANCELED("400_INVALID_ALREADY_CANCELED", HttpStatus.BAD_REQUEST, "이미 취소되거나 취소신청중인 결제입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
