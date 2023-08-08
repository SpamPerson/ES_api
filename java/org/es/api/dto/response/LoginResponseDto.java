package org.es.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.es.api.entity.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpireTime;
    private Long accessTokenExpireTime;
    private User user;
}
