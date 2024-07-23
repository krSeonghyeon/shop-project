package com.bcsd.shop.repository;

import com.bcsd.shop.domain.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
