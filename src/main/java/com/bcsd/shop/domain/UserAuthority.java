package com.bcsd.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_authority", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "authority_id"}, name = "UK_USER_AUTHORITY")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;
}
