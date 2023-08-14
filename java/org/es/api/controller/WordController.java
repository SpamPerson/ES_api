package org.es.api.controller;

import lombok.RequiredArgsConstructor;
import org.es.api.config.JwtTokenProvider;
import org.es.api.dto.WordDto;
import org.es.api.entity.User;
import org.es.api.entity.Word;
import org.es.api.repository.UserJpaRepo;
import org.es.api.repository.WordJpaRepo;
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

    /**
     * 영단어 리스트 불러오기.
     * @param request
     * @return
     */
    @GetMapping("/list")
    public List<Word> wordList(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ","");
        UUID userCode = jwtTokenProvider.parseUserCode(accessToken);
        User user = userJpaRepo.findById(userCode).orElseThrow();

        return wordJpaRepo.findAllByUserId(user.getUserId());
    }

    @PostMapping("")
    public boolean saveWord(@RequestBody WordDto wordRequestDto) {
        wordJpaRepo.save(wordRequestDto.toWord());
        return true;
    }
}
