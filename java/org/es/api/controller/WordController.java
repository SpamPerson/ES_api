package org.es.api.controller;

import lombok.RequiredArgsConstructor;
import org.es.api.config.JwtTokenProvider;
import org.es.api.dto.WordDto;
import org.es.api.entity.User;
import org.es.api.entity.Word;
import org.es.api.repository.UserJpaRepo;
import org.es.api.repository.WordJpaRepo;
import org.es.api.service.procedure.WordProcedureService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/word")
public class WordController {
    private final WordJpaRepo wordJpaRepo;
    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;

    private final WordProcedureService wordProcedureService;

    /**
     * 영단어 리스트 불러오기.
     *
     * @param request
     * @return
     * @@ [Paging 작업 요망]
     */
    @GetMapping("/list/{searchColumn}/{searchText}/{orderBy}")
    public List<Word> wordList(
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

        return wordProcedureService.prcWordList(user.getUserId(), searchText, searchColumn, orderBy);
    }

    /**
     * 단어 저장 기능
     *
     * @param wordRequestDto
     * @return
     * @@ [단어 중복 여부 확인 기능 작업 요망]
     */
    @PostMapping("")
    public boolean saveWord(@RequestBody WordDto wordRequestDto) {
        wordJpaRepo.save(wordRequestDto.toWord());
        return true;
    }


}
