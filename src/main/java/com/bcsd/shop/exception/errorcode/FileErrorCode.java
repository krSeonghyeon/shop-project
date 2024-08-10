package com.bcsd.shop.exception.errorcode;

import com.bcsd.shop.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ErrorCode {

    FILE_NOT_FOUND("404_FILE_NOT_FOUND", HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    FILE_SAVE_FAILED("500_FILE_SAVE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다."),
    FILE_READ_FAILED("500_FILE_READ_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기에 실패했습니다."),
    FILE_DELETE_FAILED("500_FILE_SAVE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제에 실패했습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
