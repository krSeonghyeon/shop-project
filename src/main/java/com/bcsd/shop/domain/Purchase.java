package com.bcsd.shop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase", uniqueConstraints = {
        @UniqueConstraint(columnNames = "payment_id", name = "UK_PURCHASE_PAYMENT")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "price", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long price;

    @Column(name = "quantity", nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer quantity;

    @Column(name = "shipping_cost", nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer shippingCost;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "request")
    private String request;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, insertable = false, columnDefinition = "ENUM('결제완료', '배송중', '구매확정', '취소요청', '취소완료', '반품요청', '반품완료', '교환요청', '교환완료') DEFAULT '결제완료'")
    private PurchaseStatus status;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public void changeStatus(PurchaseStatus status) {
        this.status = status;
    }
}
