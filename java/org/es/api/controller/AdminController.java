package org.es.api.controller;


import lombok.RequiredArgsConstructor;
import org.es.api.dto.response.SearchUserListResponseDto;
import org.es.api.entity.User;
import org.es.api.entity.UserRole;
import org.es.api.repository.UserJpaRepo;
import org.es.api.repository.UserRoleJpaRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {
    private final UserJpaRepo userJpaRepo;
    private final UserRoleJpaRepo userRoleJpaRepo;

    @PostMapping("{userId}")
    public void signupAdminRole(@PathVariable("userId") String userId) {
        User user = userJpaRepo.findByUserId(userId).orElseThrow();
        UserRole adminRole = userRoleJpaRepo.findByName("ROLE_ADMIN").orElseThrow();
        List<UserRole> roles = user.getRoles();
        roles.add(adminRole);
        user.setRoles(roles);
        userJpaRepo.save(user);
    }

    @GetMapping("/user/list/{searchColumn}/{currentPageNum}")
    public SearchUserListResponseDto searchUserList(
            @PathVariable("searchColumn") String searchColum,
            @PathVariable("currentPageNum") int currentPageNum,
            @RequestParam String searchText
    ) {
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(currentPageNum - 1, pageSize);
        List<User> users = new ArrayList<>();
        int totalPage = 0;
        switch (searchColum) {
            case "userId":
                users.addAll(userJpaRepo.findByUserIdContaining(searchText, pageable).getContent());
                totalPage = userJpaRepo.findByUserIdContaining(searchText, pageable).getTotalPages();
                break;
            case "name":
                users.addAll(userJpaRepo.findByNameContaining(searchText, pageable).getContent());
                totalPage = userJpaRepo.findByNameContaining(searchText, pageable).getTotalPages();
                break;
            case "mail":
                users.addAll(userJpaRepo.findByMailContaining(searchText, pageable).getContent());
                totalPage = userJpaRepo.findByMailContaining(searchText, pageable).getTotalPages();
                break;
        }

        return SearchUserListResponseDto.builder()
                .users(users)
                .totalPage(totalPage)
                .build();
    }

    @PostMapping("/user/list/disable")
    public boolean usersDisable(@RequestBody List<User> requestUsers) {
        List<User> users = userJpaRepo.findAllByUserIdIn(requestUsers.stream()
                .map(user -> user.getUserId())
                .collect(Collectors.toList())
        );
        users.forEach(user -> user.setIsDeleted("Y"));
        userJpaRepo.saveAll(users);
        return true;
    }

    @PostMapping("/user/list/active")
    public boolean userActive(@RequestBody List<User> requestUsers) {
        List<User> users = userJpaRepo.findAllByUserIdIn(requestUsers.stream()
                .map(user -> user.getUserId())
                .collect(Collectors.toList())
        );
        users.forEach(user -> user.setIsDeleted("N"));
        userJpaRepo.saveAll(users);
        return true;
    }

    @PutMapping("/user/list")
    public boolean userDelete(@RequestBody List<User> requestUsers) {
        List<User> users = userJpaRepo.findAllByUserIdIn(requestUsers.stream()
                .map(user -> user.getUserId())
                .collect(Collectors.toList())
        );
        userJpaRepo.deleteAll(users);
        return true;
    }
}
