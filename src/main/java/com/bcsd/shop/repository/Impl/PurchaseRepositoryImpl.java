package com.bcsd.shop.repository.Impl;

import com.bcsd.shop.domain.Purchase;
import com.bcsd.shop.repository.PurchaseRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PurchaseRepositoryImpl implements PurchaseRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public Purchase saveAndRefresh(Purchase purchase) {
        entityManager.persist(purchase);
        entityManager.flush();
        entityManager.refresh(purchase);
        return purchase;
    }
}
