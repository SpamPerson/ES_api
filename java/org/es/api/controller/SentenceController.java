package org.es.api.controller;

import lombok.RequiredArgsConstructor;
import org.es.api.config.JwtTokenProvider;
import org.es.api.entity.Sentence;
import org.es.api.entity.User;
import org.es.api.repository.SentenceJpaRepo;
import org.es.api.repository.UserJpaRepo;
import org.es.api.service.procedure.SentenceProcedureService;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sentence")
public class SentenceController {

    private final SentenceJpaRepo sentenceJpaRepo;
    private final SentenceProcedureService sentenceProcedureService;
    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/list/{searchColumn}/{searchText}/{orderBy}")
    public List<Sentence> sentenceList(
            HttpServletRequest request,
            @PathVariable("searchColumn") String searchColumn,
            @PathVariable("searchText") String searchText,
            @PathVariable("orderBy") String orderBy
    ) {
        if (searchText.equals("@empty")) {
            searchText = null;
        }
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        UUID userCode = jwtTokenProvider.parseUserCode(accessToken);
        User user = userJpaRepo.findById(userCode).orElseThrow();

        return sentenceProcedureService.prcSentenceList(user.getUserId(), searchText, searchColumn, orderBy);
    }
}
