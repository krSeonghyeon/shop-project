package com.bcsd.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/users",
                                        "/users/seller",
                                        "/auths/login"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/products/{id}",
                                        "/products/search",
                                        "/images/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/users").hasAuthority("USER")
                                .requestMatchers(HttpMethod.GET, "/users/seller").hasAuthority("SELLER")
                                .requestMatchers(HttpMethod.POST, "/products").hasAuthority("SELLER")
                                .requestMatchers(HttpMethod.GET, "/products").hasAuthority("SELLER")
                                .requestMatchers(HttpMethod.PUT, "/products/{id}").hasAuthority("SELLER")
                                .requestMatchers(HttpMethod.DELETE, "/products/{id}").hasAuthority("SELLER")
                                .requestMatchers(HttpMethod.POST, "/purchases").hasAuthority("USER")
                                .requestMatchers(HttpMethod.PUT, "/purchases/{id}/status").hasAuthority("SELLER")
                                .requestMatchers(HttpMethod.GET, "/purchases/product/{productId}").hasAuthority("SELLER")
                                .requestMatchers(HttpMethod.GET, "/purchases/user").hasAuthority("USER")
                                .requestMatchers(HttpMethod.PUT, "/purchases/{id}/cancel").hasAuthority("USER")
                                .requestMatchers(HttpMethod.PUT, "/purchases/{id}/determine").hasAuthority("USER")
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionalHandling -> exceptionalHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .userDetailsService(userDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
