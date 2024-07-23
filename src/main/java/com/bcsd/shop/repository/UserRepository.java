package com.bcsd.shop.repository;

import com.bcsd.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    boolean existsByEmail(String email);
}
