package com.bcsd.shop.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserJoinRequest(
        @Schema(example = "user1234@naver.com", description = "이메일")
        @Email(message = "이메일 형식이 올바르지 않습니다")
        @NotBlank(message = "이메일은 비어있을 수 없습니다")
        @Size(max = 100, message = "이메일은 최대 100자까지 가능합니다")
        String email,

        @Schema(example = "user1234", description = "비밀번호")
        @NotBlank(message = "비밀번호는 비어있을 수 없습니다")
        @Size(max = 255, message = "비밀번호는 최대 255자까지 가능합니다")
        String password,

        @Schema(example = "유저일", description = "이름")
        @NotBlank(message = "이름은 비어있을 수 없습니다")
        @Size(max = 50, message = "이름은 최대 50자까지 가능합니다")
        String name,

        @Schema(example = "010-1234-5678", description = "전화번호")
        @NotBlank(message = "전화번호는 비어있을 수 없습니다")
        @Size(max = 20, message = "전화번호는 최대 20자까지 가능합니다")
        String phoneNumber,

        @Schema(example = "솔빛관", description = "주소(배송지)")
        @NotBlank(message = "주소는 비어있을 수 없습니다")
        @Size(max = 255, message = "주소는 최대 255자까지 가능합니다")
        String address
) {
}
