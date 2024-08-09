package com.bcsd.shop.exception.errorcode;

import com.bcsd.shop.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {

    PRODUCT_NOT_FOUND("404_PRODUCT_NOT_FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    PRODUCT_DUPLICATED("409_PRODUCT_DUPLICATED", HttpStatus.CONFLICT, "중복 상품등록 시도입니다."),
    CATEGORY_NOT_FOUND("404_CATEGORY_NOT_FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    INVALID_PRODUCT_STATUS("400_INVALID_PRODUCT_STATUS", HttpStatus.BAD_REQUEST, "판매중인 상품이 아닙니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
