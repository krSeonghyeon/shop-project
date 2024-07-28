package com.bcsd.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name", name = "UK_CATEGORY_NAME")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
