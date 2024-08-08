package com.bcsd.shop.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserInfoModifyRequest(
        @Schema(example = "010-3333-2222", description = "변경 할 전화번호")
        @NotBlank(message = "새전화번호는 비어있을 수 없습니다")
        @Size(max = 20, message = "새전화번호는 최대 20자까지 가능합니다")
        String phoneNumber,

        @Schema(example = "해울관", description = "변경 할 주소")
        @NotBlank(message = "새주소는 비어있을 수 없습니다")
        @Size(max = 255, message = "새주소는 최대 255자까지 가능합니다")
        String address
) {
}
