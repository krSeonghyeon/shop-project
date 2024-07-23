package com.bcsd.shop.repository.Impl;

import com.bcsd.shop.domain.User;
import com.bcsd.shop.repository.UserRepositoryCustom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public User saveAndRefresh(User user) {
        entityManager.persist(user);
        entityManager.flush();
        entityManager.refresh(user);
        return user;
    }
}
