package com.bcsd.shop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment", uniqueConstraints = {
        @UniqueConstraint(columnNames = "transaction_id", name = "UK_PAYMENT_TRANSACTION")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_id", nullable = false, length = 50)
    private String transactionId;

    @Column(name = "amount", nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, columnDefinition = "ENUM('카드', '계좌이체')")
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, insertable = false, columnDefinition = "ENUM('정상결제', '취소신청', '환불완료') DEFAULT '정상결제'")
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public void changeStatus(PaymentStatus status) {
        this.status = status;
    }
}
