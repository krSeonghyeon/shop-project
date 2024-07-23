package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.UserJoinRequest;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;

public interface UserService {

    UserInfoResponse join(UserJoinRequest request);
}
