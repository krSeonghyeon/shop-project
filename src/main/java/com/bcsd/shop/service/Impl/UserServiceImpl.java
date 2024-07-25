package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.PasswordModifyRequest;
import com.bcsd.shop.controller.dto.request.SellerJoinRequest;
import com.bcsd.shop.controller.dto.request.UserJoinRequest;
import com.bcsd.shop.controller.dto.response.SellerInfoResponse;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.domain.Authority;
import com.bcsd.shop.domain.Seller;
import com.bcsd.shop.domain.User;
import com.bcsd.shop.domain.UserAuthority;
import com.bcsd.shop.repository.AuthorityRepository;
import com.bcsd.shop.repository.SellerRepository;
import com.bcsd.shop.repository.UserAuthorityRepository;
import com.bcsd.shop.repository.UserRepository;
import com.bcsd.shop.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        return UserInfoResponse.from(user);
    }

    @Override
    public SellerInfoResponse getSellerInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 판매자입니다."));

        return SellerInfoResponse.of(user, seller);
    }

    @Override
    @Transactional
    public UserInfoResponse join(UserJoinRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DataIntegrityViolationException("이미 사용 중인 이메일 입니다.");
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

    @Override
    @Transactional
    public SellerInfoResponse joinSeller(SellerJoinRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DataIntegrityViolationException("이미 사용 중인 이메일 입니다.");
        }

        if (sellerRepository.existsByBusinessNumber(request.businessNumber())) {
            throw new DataIntegrityViolationException("이미 사용 중인 사업자 등록 번호 입니다.");
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

    @Override
    @Transactional
    public void modifyPassword(Long userId, PasswordModifyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 틀렸습니다.");
        }

        String newPassword = passwordEncoder.encode(request.newPassword());
        user.changePassword(newPassword);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("존재하지 않는 회원입니다.");
        }

        userRepository.deleteById(userId);
    }

    private void assignAuthorityToUser(User user, String authorityType) {
        Authority authority = authorityRepository.findByType(authorityType)
                .orElseThrow(() -> new NoSuchElementException(authorityType + " 권한이 존재하지 않습니다"));

        UserAuthority userAuthority = UserAuthority.builder()
                .user(user)
                .authority(authority)
                .build();

        user.addAuthority(userAuthority);

        userAuthorityRepository.save(userAuthority);
    }
}
