package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    boolean existsByBusinessNumber(String businessNumber);
}
