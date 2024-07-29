package com.bcsd.shop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image")
    private String image;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long price;

    @Column(name = "shipping_cost", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer shippingCost;

    @Column(name = "stock", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, insertable = false, columnDefinition = "ENUM('판매예정', '판매중', '판매중지', '품절') DEFAULT '판매예정'")
    private ProductStatus status;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
