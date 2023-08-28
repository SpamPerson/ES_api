package org.es.api.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.es.api.entity.Sentence;
import org.es.api.entity.Word;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchSentenceResponseDto {
    List<Sentence> sentences;
    int totalPage;
}
