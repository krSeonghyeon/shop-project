package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.SellerJoinRequest;
import com.bcsd.shop.controller.dto.request.UserJoinRequest;
import com.bcsd.shop.controller.dto.response.SellerInfoResponse;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;

public interface UserService {

    Object getUserInfo(Long userId);
    UserInfoResponse join(UserJoinRequest request);
    SellerInfoResponse joinSeller(SellerJoinRequest request);
}
