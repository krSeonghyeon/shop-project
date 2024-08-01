package com.bcsd.shop.exception.errorcode;

import com.bcsd.shop.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    UNAUTHORIZED_PRODUCT("403_UNAUTHORIZED_PRODUCT", HttpStatus.UNAUTHORIZED, "해당 상품의 판매자가 아닙니다."),
    AUTHORITY_NOT_FOUND("404_AUTHORITY_NOT_FOUND", HttpStatus.NOT_FOUND, "해당 권한을 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
