package org.es.api.controller;


import lombok.RequiredArgsConstructor;
import org.es.api.config.JwtTokenProvider;
import org.es.api.dto.SignUpUserDto;
import org.es.api.dto.TokenDto;
import org.es.api.dto.request.LoginRequestDto;
import org.es.api.dto.response.LoginResponseDto;
import org.es.api.entity.RefreshToken;
import org.es.api.entity.User;
import org.es.api.entity.UserRole;
import org.es.api.repository.RefreshTokenJpaRepo;
import org.es.api.repository.UserJpaRepo;
import org.es.api.repository.UserRoleJpaRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final UserRoleJpaRepo userRoleJpaRepo;
    private final RefreshTokenJpaRepo refreshTokenJpaRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("signUp")
    public void signUp(@RequestBody SignUpUserDto signUpUserDto) {
        if(userJpaRepo.findByUserId(signUpUserDto.getUserId()).isPresent()){
            throw new RuntimeException("Already user");
        } else if (userJpaRepo.findByMail(signUpUserDto.getMail()).isPresent()){
            throw new RuntimeException("Already email");
        }

        UserRole role = userRoleJpaRepo.findByName("ROLE_USER").orElseThrow();
        List<UserRole> roles = new ArrayList<>();
        roles.add(role);

        userJpaRepo.save(signUpUserDto.toUser(passwordEncoder, roles));
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        User user = userJpaRepo.findByUserId(loginRequestDto.getUserId()).orElseThrow();
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) throw new RuntimeException("Password error");
        TokenDto tokenDto = jwtTokenProvider.createTokenDto(user.getUserCode());
        RefreshToken refreshToken;
        if(refreshTokenJpaRepo.findByUserId(user.getUserId()).isPresent()) {
            refreshToken = refreshTokenJpaRepo.findByUserId(user.getUserId()).orElseThrow();
            refreshToken.updateToken(tokenDto.getRefreshToken(), refreshToken.getTokenCode(), request.getRemoteAddr(), refreshToken.getExpireTime());
        } else {
            refreshToken = RefreshToken.builder()
                    .userId(user.getUserId())
                    .token(tokenDto.getRefreshToken())
                    .ip(request.getRemoteAddr())
                    .build();
        }
        refreshTokenJpaRepo.save(refreshToken);

        return LoginResponseDto.builder()
                .accessToken(tokenDto.getAccessToken())
                .accessTokenExpireTime(tokenDto.getAccessTokenExpireTime())
                .refreshTokenExpireTime(tokenDto.getRefreshTokenExpireTime())
                .grantType(tokenDto.getGrantType())
                .refreshToken(tokenDto.getRefreshToken())
                .user(user)
                .build();
    }

}
