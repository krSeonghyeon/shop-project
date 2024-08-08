package com.bcsd.shop.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Schema(example = "user1234@naver.com", description = "이메일")
        @Email(message = "이메일 형식이 올바르지 않습니다")
        @NotBlank(message = "이메일은 비어있을 수 없습니다")
        @Size(max = 100, message = "이메일은 100자 이하입니다")
        String email,

        @Schema(example = "user1234", description = "비밀번호")
        @NotBlank(message = "비밀번호는 비어있을 수 없습니다")
        @Size(max = 255, message = "비밀번호는 255자 이하입니다")
        String password
) {
}
