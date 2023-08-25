package org.es.api.controller;

import lombok.RequiredArgsConstructor;
import org.es.api.config.JwtTokenProvider;
import org.es.api.dto.SentenceDto;
import org.es.api.dto.response.SentenceCountResponseDto;
import org.es.api.entity.Sentence;
import org.es.api.entity.User;
import org.es.api.repository.SentenceJpaRepo;
import org.es.api.repository.UserJpaRepo;
import org.es.api.service.procedure.SentenceProcedureService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sentence")
public class SentenceController {

    private final SentenceJpaRepo sentenceJpaRepo;
    private final SentenceProcedureService sentenceProcedureService;
    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("")
    public Sentence sentenceSave(@RequestBody SentenceDto sentenceDto) {
        return sentenceJpaRepo.save(sentenceDto.toSentence());
    }

    @PutMapping("/list")
    public boolean sentenceDeleteList(@RequestBody List<SentenceDto> sentenceDtoList) {
        List<Long> sentenceCodeList = sentenceDtoList.stream()
                .map(SentenceDto::getSentenceCode)
                .collect(Collectors.toList());
        sentenceJpaRepo.deleteAllById(sentenceCodeList);
        return true;
    }

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

    @GetMapping("/count")
    public SentenceCountResponseDto sentenceCount(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        UUID userCode = jwtTokenProvider.parseUserCode(accessToken);
        User user = userJpaRepo.findById(userCode).orElseThrow();
        List<Sentence> sentenceList = sentenceJpaRepo.findAllByUserId(user.getUserId());
        int totalCount = sentenceList.size();
        int memorizeCount = (int) sentenceList.stream()
                .filter(word -> "Y".equals(word.getIsMemorize()))
                .count();
        return SentenceCountResponseDto.builder()
                .totalSentence(totalCount)
                .memorizeSentence(memorizeCount)
                .build();
    }
}
