package com.bcsd.shop.controller.dto.response;

import com.bcsd.shop.domain.Seller;
import com.bcsd.shop.domain.User;
import com.bcsd.shop.domain.UserAuthority;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record SellerInfoResponse(
        Long id,
        String email,
        String name,
        String phoneNumber,
        String address,
        String companyName,
        String businessNumber,
        Set<String> authorities,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static SellerInfoResponse of(User user, Seller seller) {
        Set<String> authorities = user.getAuthorities().stream()
                .map(UserAuthority::getAuthority)
                .collect(Collectors.toSet());

        return new SellerInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getAddress(),
                seller.getCompanyName(),
                seller.getBusinessNumber(),
                authorities,
                user.getCreatedAt()
        );
    }
}
