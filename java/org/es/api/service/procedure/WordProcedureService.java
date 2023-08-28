package org.es.api.service.procedure;

import lombok.RequiredArgsConstructor;
import org.es.api.dto.response.SearchWordListResponseDto;
import org.es.api.entity.Word;
import org.es.api.repository.WordJpaRepo;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordProcedureService {
    private final WordJpaRepo wordJpaRepo;

    public Long convertLong(BigInteger bigInteger) {
        return bigInteger.longValue();
    }

    public SearchWordListResponseDto prcWordList(String userId, String searchColumn, String searchText, int currentPageNum, int pageSize) {
        List<Object[]> prcResult = wordJpaRepo.prcListWord(userId, searchText, searchColumn, currentPageNum, pageSize);
        List<Object[]> prcCountResult = wordJpaRepo.prcCountWordList(userId, searchText, searchColumn);
        int totalCount = 0;
        List<Word> wordList = new ArrayList<>();
        for (Object[] result : prcResult) {
            wordList.add(Word.builder()
                    .wordCode(convertLong((BigInteger) result[0]))
                    .createDate((String) result[1])
                    .enWord((String) result[2])
                    .isMemorize(((Character) result[3]).toString())
                    .krWord((String) result[4])
                    .remarks((String) result[5])
                    .userId((String) result[6])
                    .build()
            );
        }

        for(Object[] result : prcCountResult) {
            totalCount = ((BigInteger) result[0]).intValue();
        }
        int totalPage = Math.max((int) Math.ceil((double) totalCount / pageSize) , 1);

        return SearchWordListResponseDto.builder()
                .words(wordList)
                .totalPage(totalPage)
                .build();
    }

    public List<Word> prcUpdateWord(String wordCode, String columnName, String value) {
        List<Object[]> prcResult = wordJpaRepo.prcUpdateWord(wordCode, columnName, value);
        List<Word> wordList = new ArrayList<>();
        for (Object[] result : prcResult) {
            wordList.add(Word.builder()
                    .wordCode(convertLong((BigInteger) result[0]))
                    .createDate((String) result[1])
                    .enWord((String) result[2])
                    .isMemorize(((Character) result[3]).toString())
                    .krWord((String) result[4])
                    .remarks((String) result[5])
                    .userId((String) result[6])
                    .build()
            );
        }
        return wordList;
    }

    public List<Word> prcSelectWordByEnWord(String userId, String enWord) {
        List<Object[]> prcResult = wordJpaRepo.prcSelectWordByEnWord(userId, enWord);
        List<Word> wordList = new ArrayList<>();
        for (Object[] result : prcResult) {
            wordList.add(Word.builder()
                    .wordCode(convertLong((BigInteger) result[0]))
                    .createDate((String) result[1])
                    .enWord((String) result[2])
                    .isMemorize(((Character) result[3]).toString())
                    .krWord((String) result[4])
                    .remarks((String) result[5])
                    .userId((String) result[6])
                    .build()
            );
        }
        return wordList;
    }

}
