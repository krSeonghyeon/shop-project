package com.bcsd.shop.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seller", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id", name = "UK_SELLER_USER"),
        @UniqueConstraint(columnNames = "business_number", name = "UK_SELLER_BUSINESS_NUMBER")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // 데이터베이스 ON DELETE CASCADE, 양방향 관계 회피
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "business_number", nullable = false, length = 20)
    private String businessNumber;
}
