package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom, JpaSpecificationExecutor<Product> {

    List<Product> findAllBySellerId(Long sellerId);
}
