package org.es.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_SENTENCE")
public class Sentence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "SENTENCE_CODE")
    private Long sentenceCode;

    @Column(nullable = false, name = "USER_ID")
    private String userId;

    @Column(nullable = false, name = "EN_SENTENCE")
    private String enSentence;

    @Column(nullable = false, name = "KR_SENTENCE")
    private String krSentence;

    @Column(nullable = false, name = "SENTENCE_CREATE_DATE")
    private String createDate;

    @Column(columnDefinition = "char(1) default 'N'",name = "SENTENCE_IS_MEMORIZE")
    private String isMemorize;
}
