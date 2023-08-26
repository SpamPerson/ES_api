package org.es.api.controller;


import lombok.RequiredArgsConstructor;
import org.es.api.entity.User;
import org.es.api.entity.UserRole;
import org.es.api.repository.UserJpaRepo;
import org.es.api.repository.UserRoleJpaRepo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {
    private final UserJpaRepo userJpaRepo;
    private final UserRoleJpaRepo userRoleJpaRepo;

    @PostMapping("{userId}")
    public void signupAdminRole (@PathVariable("userId") String userId) {
        User user = userJpaRepo.findByUserId(userId).orElseThrow();
        UserRole adminRole = userRoleJpaRepo.findByName("ROLE_ADMIN").orElseThrow();
        List<UserRole> roles = user.getRoles();
        roles.add(adminRole);
        user.setRoles(roles);
        userJpaRepo.save(user);
    }
}
