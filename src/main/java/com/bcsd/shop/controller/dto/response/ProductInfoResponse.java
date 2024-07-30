package com.bcsd.shop.controller.dto.response;

import com.bcsd.shop.domain.Product;
import com.bcsd.shop.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProductInfoResponse (
    Long id,
    String categoryName,
    String userName,
    String productName,
    String image,
    String description,
    Long price,
    Integer shippingCost,
    Integer stock,
    ProductStatus status,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt
) {
    public static ProductInfoResponse from(Product product) {
        return new ProductInfoResponse(
                product.getId(),
                product.getCategory().getName(),
                product.getSeller().getName(),
                product.getName(),
                product.getImage(),
                product.getDescription(),
                product.getPrice(),
                product.getShippingCost(),
                product.getStock(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
