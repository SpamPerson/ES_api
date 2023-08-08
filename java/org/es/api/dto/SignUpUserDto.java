package org.es.api.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.es.api.entity.User;
import org.es.api.entity.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SignUpUserDto {
    String userId;
    String password;
    String name;
    String mail;

    public User toUser(PasswordEncoder passwordEncoder, List<UserRole> roles){
        return User.builder()
                .userId(userId)
                .password(passwordEncoder.encode(password))
                .mail(mail)
                .name(name)
                .roles(roles)
                .isDeleted("N")
                .build();
    }

}
