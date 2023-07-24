package org.es.api.config;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.es.api.dto.TokenDto;
import org.es.api.repository.UserJpaRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    /**
     *  1000L 1초
     */
    private final Long accessTokenValidTime = 1 * 60 * 1000L;
    private final Long refreshTokenValidTime = 15 * 24 * 60 * 60 * 1000L;
    private final UserJpaRepo userJpaRepo;
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //token 생성
    public TokenDto createTokenDto(UUID userCode){
        Claims claims = Jwts.claims().setSubject(String.valueOf(userCode));
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireTime(accessTokenValidTime)
                .refreshTokenExpireTime(refreshTokenValidTime)
                .build();
    }

    public Authentication getAuthentication(String token){
        Claims claims = parseClaims(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    public UUID parseUserCode(String token) {
        try {
            String userCode = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            return UUID.fromString(userCode);
        }catch(ExpiredJwtException e){
            throw e;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String token;
        if(request.getHeader("Authorization") != null && request.getHeader("Authorization").startsWith("Bearer ")){
            token = request.getHeader("Authorization").replace("Bearer ","");
        } else {
            token = null;
        }
        return token;
    }

    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e){
            System.out.println(e.toString());
            return false;
        }
    }
}
