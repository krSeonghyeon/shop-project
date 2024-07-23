package com.bcsd.shop.repository;

import com.bcsd.shop.domain.User;

public interface UserRepositoryCustom {

    User saveAndRefresh(User user);
}
