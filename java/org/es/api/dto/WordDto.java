package org.es.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.es.api.entity.Word;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WordDto {
    String userId;
    String enWord;
    String krWord;
    String createDate;
    String isMemorize;
    String remarks;

    public Word toWord(){
        return Word.builder()
                .userId(userId)
                .enWord(enWord)
                .krWord(krWord)
                .createDate(createDate)
                .remarks(remarks)
                .isMemorize("N")
                .build();
    }
}
