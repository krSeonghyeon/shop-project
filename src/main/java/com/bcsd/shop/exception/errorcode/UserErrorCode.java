package com.bcsd.shop.exception.errorcode;

import com.bcsd.shop.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    USER_NOT_FOUND("404_USER_NOT_FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    SELLER_NOT_FOUND("404_SELLER_NOT_FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 판매자입니다."),
    EMAIL_DUPLICATED("409_EMAIL_DUPLICATED", HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    BUSINESS_NUM_DUPLICATED("409_BUSINESS_NUM_DUPLICATED", HttpStatus.CONFLICT, "이미 사용중인 사업자등록번호입니다."),
    INVALID_PASSWORD("400_INVALID_PASSWORD", HttpStatus.BAD_REQUEST, "현재 비밀번호가 틀렸습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
