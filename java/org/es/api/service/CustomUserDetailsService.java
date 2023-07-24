package org.es.api.service;

import lombok.RequiredArgsConstructor;
import org.es.api.entity.User;
import org.es.api.repository.UserJpaRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepo userJpaRepo;

    @Override
    public UserDetails loadUserByUsername(String userCode) throws UsernameNotFoundException {
        UUID code = UUID.fromString(userCode);
        User user = userJpaRepo.findById(code).orElseThrow();
        return user;
    }
}
