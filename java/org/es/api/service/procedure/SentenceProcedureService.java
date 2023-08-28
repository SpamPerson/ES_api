package org.es.api.service.procedure;

import lombok.RequiredArgsConstructor;
import org.es.api.dto.response.SearchSentenceResponseDto;
import org.es.api.entity.Sentence;
import org.es.api.repository.SentenceJpaRepo;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SentenceProcedureService {
    private final SentenceJpaRepo sentenceJpaRepo;

    public Long convertLong(BigInteger bigInteger) {
        return bigInteger.longValue();
    }

    public SearchSentenceResponseDto prcSentenceList(String userId, String searchText, String searchColumn, int currentPageNum) {
        int pageSize = 10;
        int totalCount = 0;
        int totalPage = 0;
        List<Object[]> prcResult = sentenceJpaRepo.prcListSentence(userId, searchText, searchColumn, currentPageNum, pageSize);
        List<Object[]> prcCountResult = sentenceJpaRepo.prcCountSentence(userId, searchText, searchColumn);

        List<Sentence> sentenceList = new ArrayList<>();
        for (Object[] result : prcResult) {
            sentenceList.add(Sentence.builder()
                    .sentenceCode(convertLong((BigInteger) result[0]))
                    .createDate((String) result[1])
                    .enSentence((String) result[2])
                    .isMemorize(((Character) result[3]).toString())
                    .krSentence((String) result[4])
                    .userId((String) result[5])
                    .remarks((String) result[6])
                    .build()
            );
        }

        for (Object[] result : prcCountResult) {
            totalCount = ((BigInteger) result[0]).intValue();
        }
        totalPage = Math.max((int) Math.ceil((double) totalCount / pageSize), 1);

        return SearchSentenceResponseDto.builder()
                .sentences(sentenceList)
                .totalPage(totalPage)
                .build();
    }

}
