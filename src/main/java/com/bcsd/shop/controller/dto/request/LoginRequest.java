package com.bcsd.shop.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email(message = "이메일 형식이 올바르지 않습니다")
        @NotBlank(message = "이메일은 비어있을 수 없습니다")
        @Size(max = 100, message = "이메일은 100자 이하입니다")
        String email,

        @NotBlank(message = "비밀번호는 비어있을 수 없습니다")
        @Size(max = 255, message = "비밀번호는 255자 이하입니다")
        String password
) {
}
