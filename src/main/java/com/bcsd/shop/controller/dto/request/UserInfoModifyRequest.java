package com.bcsd.shop.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserInfoModifyRequest(
        @NotBlank(message = "새전화번호는 비어있을 수 없습니다")
        @Size(max = 20, message = "새전화번호는 최대 20자까지 가능합니다")
        String phoneNumber,

        @NotBlank(message = "새주소는 비어있을 수 없습니다")
        @Size(max = 255, message = "새주소는 최대 255자까지 가능합니다")
        String address
) {
}
