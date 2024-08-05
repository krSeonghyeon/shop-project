package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long>, PurchaseRepositoryCustom {

    List<Purchase> findAllByProductId(Long productId);
}
