package org.es.api.service.procedure;

import lombok.RequiredArgsConstructor;
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

    public List<Word> prcWordList(String userId, String searchText, String searchColumn, String oderBy) {
        System.out.println(userId);
        System.out.println(searchText);
        System.out.println(searchColumn);
        System.out.println(oderBy);

        List<Object[]> prcResult = wordJpaRepo.prcListWord(userId, searchText, searchColumn, oderBy);
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
