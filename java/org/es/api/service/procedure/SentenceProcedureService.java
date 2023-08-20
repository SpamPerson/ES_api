package org.es.api.service.procedure;

import lombok.RequiredArgsConstructor;
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

    public List<Sentence> prcSentenceList(String userId, String searchText, String searchColumn, String orderBy) {
        List<Object[]> prcResult = sentenceJpaRepo.prcListSentence(userId, searchText, searchColumn, orderBy);
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
        return sentenceList;
    }

}
