package org.es.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.es.api.entity.Sentence;
import org.es.api.entity.Word;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SentenceDto {

    Long sentenceCode;

    String userId;

    String enSentence;

    String krSentence;

    String createDate;

    String isMemorize;

    String remarks;

    public Sentence toSentence() {
        return Sentence.builder()
                .userId(userId)
                .krSentence(krSentence)
                .enSentence(enSentence)
                .remarks(remarks)
                .isMemorize(isMemorize)
                .createDate(createDate)
                .build();
    }
}
