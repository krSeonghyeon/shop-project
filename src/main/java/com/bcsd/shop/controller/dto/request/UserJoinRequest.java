package com.bcsd.shop.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserJoinRequest(
        @Email(message = "이메일 형식이 올바르지 않습니다")
        @NotBlank(message = "이메일은 비어있을 수 없습니다")
        @Size(max = 100, message = "이메일은 최대 100자까지 가능합니다")
        String email,

        @NotBlank(message = "비밀번호는 비어있을 수 없습니다")
        @Size(max = 255, message = "비밀번호는 최대 255자까지 가능합니다")
        String password,

        @NotBlank(message = "이름은 비어있을 수 없습니다")
        @Size(max = 50, message = "이름은 최대 50자까지 가능합니다")
        String name,

        @NotBlank(message = "전화번호는 비어있을 수 없습니다")
        @Size(max = 20, message = "전화번호는 최대 20자까지 가능합니다")
        String phoneNumber,

        @NotBlank(message = "주소는 비어있을 수 없습니다")
        @Size(max = 255, message = "주소는 최대 255자까지 가능합니다")
        String address
) {
}
