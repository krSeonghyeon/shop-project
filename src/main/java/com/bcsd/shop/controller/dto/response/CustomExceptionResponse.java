package com.bcsd.shop.controller.dto.response;

import com.bcsd.shop.exception.ErrorCode;

public record CustomExceptionResponse(
        String errorCode,
        int status,
        String message
) {
    public static CustomExceptionResponse from(ErrorCode code) {
        return new CustomExceptionResponse(
                code.getCode(),
                code.getHttpStatus().value(),
                code.getMessage()
        );
    }
}
