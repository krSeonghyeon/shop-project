package com.bcsd.shop.controller.dto.response;

import com.bcsd.shop.domain.Product;
import com.bcsd.shop.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProductSimpleInfoResponse(
        Long id,
        String categoryName,
        String userName,
        String productName,
        String image,
        Long price,
        ProductStatus productStatus,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {

    public static ProductSimpleInfoResponse from(Product product) {
        return new ProductSimpleInfoResponse(
                product.getId(),
                product.getCategory().getName(),
                product.getSeller().getName(),
                product.getName(),
                product.getImage(),
                product.getPrice(),
                product.getStatus(),
                product.getUpdatedAt()
        );
    }
}
