package com.bcsd.shop.repository;

import com.bcsd.shop.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByType(String type);
}
