package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findAllBySellerId(Long sellerId);
}
