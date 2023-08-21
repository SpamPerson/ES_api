package org.es.api.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SentenceCountResponseDto {
    int totalSentence;
    int memorizeSentence;
}
