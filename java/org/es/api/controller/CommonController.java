package org.es.api.controller;

import lombok.RequiredArgsConstructor;
import org.es.api.repository.UserJpaRepo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class CommonController {
    private final UserJpaRepo userJpaRepo;

}
