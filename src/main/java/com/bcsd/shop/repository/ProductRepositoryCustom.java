package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Product;

public interface ProductRepositoryCustom {

    Product saveAndRefresh(Product product);
}
