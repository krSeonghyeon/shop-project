package com.bcsd.shop.repository.Impl;

import com.bcsd.shop.domain.Product;
import com.bcsd.shop.repository.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Product saveAndRefresh(Product product) {
        entityManager.persist(product);
        entityManager.flush();
        entityManager.refresh(product);
        return product;
    }
}
