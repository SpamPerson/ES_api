package org.es.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_REFRESH_TOKEN")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_CODE")
    private Long tokenCode;

    @Column(name = "USER_ID")
    private String userId;

    @Column(length = 255, nullable = false, name = "REFRESH_TOKEN")
    private String token;

    @Column(length = 255, nullable = false, name = "TOKEN_EXPIRE_TIME")
    private Long expireTime;

    @Column(nullable = false, name = "TOKEN_CREATE_DATE")
    private String createDate;

    @Column(nullable = true, name = "ACCESS_IP")
    private String ip;

    public RefreshToken updateToken(String token,Long tokenCode, String ip, Long expireTime) {
        this.tokenCode = tokenCode;
        this.ip = ip;
        this.token = token;
        this.createDate = new Date().toString();
        this.expireTime = expireTime;
        return this;
    }

    @Builder
    public RefreshToken(String userId, String token,Long expireTime, String ip) {
        this.ip = ip;
        this.userId = userId;
        this.token = token;
        this.expireTime = expireTime;
        this.createDate = new Date().toString();
    }

}
