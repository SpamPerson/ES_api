package org.es.api.controller;

import lombok.RequiredArgsConstructor;
import org.es.api.config.JwtTokenProvider;
import org.es.api.dto.WordDto;
import org.es.api.dto.request.UpdateWordRequestDto;
import org.es.api.dto.response.WordCountResponseDto;
import org.es.api.entity.User;
import org.es.api.entity.Word;
import org.es.api.repository.UserJpaRepo;
import org.es.api.repository.WordJpaRepo;
import org.es.api.service.procedure.WordProcedureService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/word")
public class WordController {
    private final WordJpaRepo wordJpaRepo;
    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;

    private final WordProcedureService wordProcedureService;

    /**
     * 영단어 수정
     *
     * @param updateWordRequestDto
     * @param request
     * @return
     */
    @PatchMapping("")
    public Word updateWord(@RequestBody UpdateWordRequestDto updateWordRequestDto, HttpServletRequest request) {
        if (updateWordRequestDto.getColumnName().equals("enWord") && overlapEnWord(request, updateWordRequestDto.getValue())) {
            throw new RuntimeException("This word is existing");
        }
        List<Word> wordList = wordProcedureService.prcUpdateWord(updateWordRequestDto.getWordCode(), updateWordRequestDto.getColumnName(), updateWordRequestDto.getValue());
        return wordList.get(0);
    }

    /**
     * 단어 저장 기능
     *
     * @param wordRequestDto
     * @return
     * @@ [단어 중복 여부 확인 기능 작업 요망]
     */
    @PostMapping("")
    public Word saveWord(@RequestBody WordDto wordRequestDto, HttpServletRequest request) {
        if (overlapEnWord(request, wordRequestDto.getEnWord())) {
            throw new RuntimeException("This word is existing");
        }

        return wordJpaRepo.save(wordRequestDto.toWord());
    }

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
     * 영단어 대량 삭제
     *
     * @param wordListDto
     * @return
     */
    @PutMapping("/list")
    public boolean deleteWordList(@RequestBody List<WordDto> wordListDto) {
        List<Long> wordCodeList = wordListDto.stream()
                .map(WordDto::getWordCode)
                .collect(Collectors.toList());

        List<Word> wordList = wordJpaRepo.findAllById(wordCodeList);

        for (Word word : wordList) {
            wordJpaRepo.delete(word);
        }
        return true;
    }

    @GetMapping("/count")
    public WordCountResponseDto wordCount(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        UUID userCode = jwtTokenProvider.parseUserCode(accessToken);
        User user = userJpaRepo.findById(userCode).orElseThrow();
        List<Word> wordList = wordJpaRepo.findAllByUserId(user.getUserId());
        int totalCount = wordList.size();
        int memorizeCount = (int) wordList.stream()
                .filter(word -> "Y".equals(word.getIsMemorize()))
                .count();
        return WordCountResponseDto.builder()
                .totalWord(totalCount)
                .memorizeWord(memorizeCount)
                .build();
    }

    public boolean overlapEnWord(HttpServletRequest request, String word) {
        boolean reault = false;
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        UUID userCode = jwtTokenProvider.parseUserCode(accessToken);
        User user = userJpaRepo.findById(userCode).orElseThrow();

        if (wordProcedureService.prcSelectWordByEnWord(user.getUserId(), word).size() > 0) {
            reault = true;
        }
        return reault;
    }

}
