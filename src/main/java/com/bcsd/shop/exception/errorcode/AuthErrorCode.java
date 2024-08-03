package com.bcsd.shop.exception.errorcode;

import com.bcsd.shop.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    FORBIDDEN_PRODUCT("403_FORBIDDEN_PRODUCT", HttpStatus.FORBIDDEN, "해당 상품의 판매자가 아닙니다."),
    AUTHORITY_NOT_FOUND("404_AUTHORITY_NOT_FOUND", HttpStatus.NOT_FOUND, "해당 권한을 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS("401_UNAUTHORIZED_ACCESS", HttpStatus.UNAUTHORIZED, "로그인을 먼저 진행해야 합니다"),
    AUTHENTICATION_FAILED("401_AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 틀렸습니다."),
    AUTHENTICATION_ERROR("401_AUTHENTICATION_ERROR", HttpStatus.UNAUTHORIZED, "계정인증에 실패했습니다."),
    FORBIDDEN_ACCESS("403_FORBIDDEN_ACCESS", HttpStatus.FORBIDDEN, "해당 접근에 대한 권한이 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
