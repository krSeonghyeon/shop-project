package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    boolean existsByBusinessNumber(String businessNumber);
    Optional<Seller> findByUserId(Long userId);
}
