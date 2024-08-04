package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Purchase;

public interface PurchaseRepositoryCustom {

    Purchase saveAndRefresh(Purchase purchase);
}
