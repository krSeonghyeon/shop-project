package com.bcsd.shop.repository;

import com.bcsd.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
