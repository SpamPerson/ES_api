package org.es.api.controller;

import lombok.RequiredArgsConstructor;
import org.es.api.config.JwtTokenProvider;
import org.es.api.dto.SignUpUserDto;
import org.es.api.dto.TokenDto;
import org.es.api.dto.request.FindPasswordDto;
import org.es.api.dto.request.LoginRequestDto;
import org.es.api.dto.response.LoginResponseDto;
import org.es.api.entity.RefreshToken;
import org.es.api.entity.User;
import org.es.api.entity.UserRole;
import org.es.api.repository.RefreshTokenJpaRepo;
import org.es.api.repository.UserJpaRepo;
import org.es.api.repository.UserRoleJpaRepo;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final UserRoleJpaRepo userRoleJpaRepo;
    private final RefreshTokenJpaRepo refreshTokenJpaRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @PostMapping("/adminRole/{userId}")
    public void signupAdminRole (@PathVariable("userId") String userId) {
        User user = userJpaRepo.findByUserId(userId).orElseThrow();
        UserRole adminRole = userRoleJpaRepo.findByName("ROLE_ADMIN").orElseThrow();
        List<UserRole> roles = user.getRoles();
        roles.add(adminRole);
        user.setRoles(roles);
        userJpaRepo.save(user);
    }

    /**
     * 회원가입
     *
     * @param signUpUserDto
     */
    @PostMapping("/signup")
    public void signUp(@RequestBody SignUpUserDto signUpUserDto) {
        if (userJpaRepo.findByUserId(signUpUserDto.getUserId()).isPresent()) {
            throw new RuntimeException("Already user");
        } else if (userJpaRepo.findByMail(signUpUserDto.getMail()).isPresent()) {
            throw new RuntimeException("Already email");
        }

        UserRole role = userRoleJpaRepo.findByName("ROLE_USER").orElseThrow();
        List<UserRole> roles = new ArrayList<>();
        roles.add(role);

        userJpaRepo.save(signUpUserDto.toUser(passwordEncoder, roles));
    }

    /**
     * 로그인
     *
     * @param loginRequestDto
     * @param request
     * @return
     */
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        User user = userJpaRepo.findByUserId(loginRequestDto.getUserId()).orElseThrow();

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password error");
        }
        TokenDto tokenDto = jwtTokenProvider.createTokenDto(user.getUserCode());
        RefreshToken refreshToken;
        if (refreshTokenJpaRepo.findByUserId(user.getUserId()).isPresent()) {
            refreshToken = refreshTokenJpaRepo.findByUserId(user.getUserId()).orElseThrow();
            refreshToken.updateToken(tokenDto.getRefreshToken(), refreshToken.getTokenCode(), request.getRemoteAddr(), refreshToken.getExpireTime());
        } else {
            refreshToken = RefreshToken.builder()
                    .userId(user.getUserId())
                    .expireTime(tokenDto.getRefreshTokenExpireTime())
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

    /**
     * 비밀번호 찾기
     *
     * @param findPasswordDto
     * @return
     */
    @PostMapping("findpassword")
    public boolean findPassword(@RequestBody FindPasswordDto findPasswordDto) {
        try {
            User user = userJpaRepo.findByMail(findPasswordDto.getMail()).orElseThrow();
            String newPassword = newPassword();
            MimeMessage sentMail = javaMailSender.createMimeMessage();
            sentMail.addRecipients(MimeMessage.RecipientType.TO, findPasswordDto.getMail());
            sentMail.setSubject("[ES] 비밀번호 찾기");
            sentMail.setText(setContext(newPassword, user.getUserId()), "utf-8", "html");
            userJpaRepo.save(User.builder()
                    .userCode(user.getUserCode())
                    .userId(user.getUserId())
                    .password(passwordEncoder.encode(newPassword))
                    .roles(user.getRoles())
                    .mail(user.getMail())
                    .name(user.getName())
                    .isDeleted(user.getIsDeleted())
                    .build());
            javaMailSender.send(sentMail);

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 유저 아이디 중복 체크
     *
     * @param userId
     * @return
     */
    @GetMapping("/check/id/{userId}")
    public boolean checkUserId(@PathVariable("userId") String userId) {
        boolean result = false;
        if (userJpaRepo.findByUserId(userId).isPresent()) {
            result = true;
        }
        return result;
    }

    @GetMapping("/check/mail/{mail}")
    public boolean checkMail(@PathVariable("mail") String mail) {
        boolean result = false;
        if (userJpaRepo.findByMail(mail).isPresent()) {
            result = true;
        }
        return result;
    }


    /**
     * RefreshToken 을 이용한 로그인
     *
     * @param request
     * @return
     */
    @GetMapping("reconnect")
    public LoginResponseDto reconnect(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String refreshToken = request.getHeader("Authorization").replace("Basic ", "");

        if (!jwtTokenProvider.validationToken(refreshToken)) {
            throw new RuntimeException("Token is not valid");
        }
        UUID userCode = jwtTokenProvider.parseUserCode(refreshToken);
        User user = userJpaRepo.findById(userCode).orElseThrow();
        if (user.getIsDeleted() == "Y") {
            throw new RuntimeException("deleted user");
        }
        RefreshToken findRefreshToken = refreshTokenJpaRepo.findByUserId(user.getUserId()).orElseThrow();
        TokenDto newToken = jwtTokenProvider.createTokenDto(user.getUserCode());

        refreshTokenJpaRepo.save(findRefreshToken.updateToken(newToken.getRefreshToken(), findRefreshToken.getTokenCode(), ipAddress, newToken.getRefreshTokenExpireTime()));

        return LoginResponseDto.builder()
                .accessToken(newToken.getAccessToken())
                .accessTokenExpireTime(newToken.getAccessTokenExpireTime())
                .refreshTokenExpireTime(newToken.getRefreshTokenExpireTime())
                .grantType(newToken.getGrantType())
                .refreshToken(newToken.getRefreshToken())
                .user(user)
                .build();
    }

    @PostMapping("password/change")
    public boolean changePassword(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userJpaRepo.findByUserId(loginRequestDto.getUserId()).orElseThrow();
        userJpaRepo.save(User.builder()
                .userCode(user.getUserCode())
                .userId(user.getUserId())
                .name(user.getName())
                .password(passwordEncoder.encode(loginRequestDto.getPassword()))
                .roles(user.getRoles())
                .mail(user.getMail())
                .isDeleted(user.getIsDeleted())
                .build());

        return true;
    }

    @GetMapping("info")
    public User getUserInfoByAccessToken(HttpServletRequest request){
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        if (!jwtTokenProvider.validationToken(accessToken)) {
            throw new RuntimeException("Token is not valid");
        }
        UUID userCode = jwtTokenProvider.parseUserCode(accessToken);
        User user = userJpaRepo.findById(userCode).orElseThrow();
        if(user.getIsDeleted() == "Y") {
            throw new RuntimeException("User deleted");
        }
        return user;
    }

    private String newPassword() {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            if (random.nextBoolean()) {
                stringBuffer.append((char) ((int) (random.nextInt(26)) + 97));
            } else {
                stringBuffer.append(random.nextInt(10));
            }
        }

        return stringBuffer.toString();
    }

    private String setContext(String password, String userId) {
        Context context = new Context();
        String maskedId = userId.substring(0, userId.length() - 2) + "**";
        context.setVariable("password", password);
        context.setVariable("userId", maskedId);
        return templateEngine.process("mail", context);
    }

}
