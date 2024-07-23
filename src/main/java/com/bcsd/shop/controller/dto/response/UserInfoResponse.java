package com.bcsd.shop.controller.dto.response;

import com.bcsd.shop.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record UserInfoResponse(
        Long id,
        String email,
        String name,
        String phoneNumber,
        String address,
        Set<String> authorities,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
        public static UserInfoResponse from(User user) {
                Set<String> authorities = user.getAuthorities().stream()
                        .map(authority -> authority.getAuthority().getType())
                        .collect(Collectors.toSet());

                return new UserInfoResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getPhoneNumber(),
                        user.getAddress(),
                        authorities,
                        user.getCreatedAt()
                );
        }
}
