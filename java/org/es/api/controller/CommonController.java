package org.es.api.controller;

import lombok.RequiredArgsConstructor;
import org.es.api.dto.request.SettingRequestDto;
import org.es.api.entity.UserRole;
import org.es.api.repository.UserJpaRepo;
import org.es.api.repository.UserRoleJpaRepo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class CommonController {

    private final UserRoleJpaRepo userRoleJpaRepo;

    @PostMapping("/setting")
    public boolean setting(@RequestBody List<SettingRequestDto> settingRequestDtoList) {
        Boolean result = false;
        for (int i = 0; i < settingRequestDtoList.size(); ++i) {
            if (userRoleJpaRepo.findByName(settingRequestDtoList.get(i).getRoleName()).isPresent()) {
                throw new RuntimeException("already role");
            } else if (!settingRequestDtoList.get(i).getRoleName().startsWith("ROLE_")) {
                throw new RuntimeException("Invalid RoleName rule");
            }

            userRoleJpaRepo.save(UserRole.builder()
                    .name(settingRequestDtoList.get(i).getRoleName())
                    .description(settingRequestDtoList.get(i).getRoleDescription())
                    .build());
            if(i == settingRequestDtoList.size()-1){
                result = true;
            }
        }

        return result;
    }
}
