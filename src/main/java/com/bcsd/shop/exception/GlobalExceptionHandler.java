package com.bcsd.shop.exception;

import com.bcsd.shop.controller.dto.response.CustomExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomExceptionResponse> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        CustomExceptionResponse response = CustomExceptionResponse.from(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomExceptionResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<String> list = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            list.add(error.getDefaultMessage());
        });

        CustomExceptionResponse response = new CustomExceptionResponse(
                "400_VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST.value(),
                list.toString()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
