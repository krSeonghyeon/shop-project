package com.bcsd.shop.service.Impl;

import com.bcsd.shop.controller.dto.request.UserJoinRequest;
import com.bcsd.shop.controller.dto.response.UserInfoResponse;
import com.bcsd.shop.domain.Authority;
import com.bcsd.shop.domain.User;
import com.bcsd.shop.domain.UserAuthority;
import com.bcsd.shop.repository.AuthorityRepository;
import com.bcsd.shop.repository.UserAuthorityRepository;
import com.bcsd.shop.repository.UserRepository;
import com.bcsd.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final PasswordEncoder passwordEncoder;

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

    private void assignAuthorityToUser(User user, String authorityType) {
        Authority authority = authorityRepository.findByType(authorityType)
                .orElseThrow(() -> new NoSuchElementException(authorityType +" 권한이 존재하지 않습니다"));

        UserAuthority userAuthority = UserAuthority.builder()
                .user(user)
                .authority(authority)
                .build();

        user.addAuthority(userAuthority);

        userAuthorityRepository.save(userAuthority);
    }
}
