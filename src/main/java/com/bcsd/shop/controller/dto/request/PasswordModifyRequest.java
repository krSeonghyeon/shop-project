package com.bcsd.shop.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordModifyRequest(
        @Schema(example = "bcsd1234", description = "기존 비밀번호")
        @NotBlank(message = "비밀번호는 비어있을 수 없습니다")
        @Size(max = 255, message = "비밀번호는 225자 이하입니다")
        String oldPassword,

        @Schema(example = "bcsd1111", description = "변경 할 비밀번호")
        @NotBlank(message = "새 비밀번호는 비어있을 수 없습니다")
        @Size(max = 255, message = "새 비밀번호는 최대 255자까지 가능합니다")
        String newPassword
) {
}
