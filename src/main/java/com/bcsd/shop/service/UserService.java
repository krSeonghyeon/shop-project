package com.bcsd.shop.service;

import com.bcsd.shop.controller.dto.request.PasswordModifyRequest;
import com.bcsd.shop.controller.dto.request.SellerJoinRequest;
import com.bcsd.shop.controller.dto.request.UserInfoModifyRequest;
import com.bcsd.shop.controller.dto.request.UserJoinRequest;
import com.bcsd.shop.controller.dto.response.SellerInfoResponse;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.domain.Authority;
import com.bcsd.shop.domain.Seller;
import com.bcsd.shop.domain.User;
import com.bcsd.shop.domain.UserAuthority;
import com.bcsd.shop.exception.CustomException;
import com.bcsd.shop.repository.AuthorityRepository;
import com.bcsd.shop.repository.SellerRepository;
import com.bcsd.shop.repository.UserAuthorityRepository;
import com.bcsd.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bcsd.shop.exception.errorcode.AuthErrorCode.AUTHORITY_NOT_FOUND;
import static com.bcsd.shop.exception.errorcode.UserErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return UserInfoResponse.from(user);
    }

    public SellerInfoResponse getSellerInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(SELLER_NOT_FOUND));

        return SellerInfoResponse.of(user, seller);
    }

    @Transactional
    public UserInfoResponse join(UserJoinRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(EMAIL_DUPLICATED);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User newUser = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .build();

        User savedUser = userRepository.saveAndRefresh(newUser);

        assignAuthorityToUser(savedUser, "USER");

        return UserInfoResponse.from(savedUser);
    }

    @Transactional
    public SellerInfoResponse joinSeller(SellerJoinRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(EMAIL_DUPLICATED);
        }

        if (sellerRepository.existsByBusinessNumber(request.businessNumber())) {
            throw new CustomException(BUSINESS_NUM_DUPLICATED);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User newUser = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .build();

        User savedUser = userRepository.saveAndRefresh(newUser);

        assignAuthorityToUser(savedUser, "SELLER");

        Seller newSeller = Seller.builder()
                .user(savedUser)
                .companyName(request.companyName())
                .businessNumber(request.businessNumber())
                .build();

        Seller savedSeller = sellerRepository.save(newSeller);

        return SellerInfoResponse.of(savedUser, savedSeller);
    }

    @Transactional
    public void modifyPassword(Long userId, PasswordModifyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new CustomException(INVALID_PASSWORD);
        }

        String newPassword = passwordEncoder.encode(request.newPassword());
        user.changePassword(newPassword);
    }

    @Transactional
    public UserInfoResponse modifyUserInfo(Long userId, UserInfoModifyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        user.changeUserInfo(request.phoneNumber(), request.address());
        return UserInfoResponse.from(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(USER_NOT_FOUND);
        }

        userRepository.deleteById(userId);
    }

    private void assignAuthorityToUser(User user, String authorityType) {
        Authority authority = authorityRepository.findByType(authorityType)
                .orElseThrow(() -> new CustomException(AUTHORITY_NOT_FOUND));

        UserAuthority userAuthority = UserAuthority.builder()
                .user(user)
                .authority(authority)
                .build();

        user.addAuthority(userAuthority);

        userAuthorityRepository.save(userAuthority);
    }
}
