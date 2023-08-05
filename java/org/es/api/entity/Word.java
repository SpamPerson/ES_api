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
@Table(name = "TB_WORD")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "WORD_CODE")
    private Long wordCode;

    @Column(nullable = false, name = "USER_ID")
    private String userId;

    @Column(nullable = false, name = "EN_WORD")
    private String enWord;

    @Column(nullable = false, name = "KR_WORD")
    private String krWord;

    @Column(nullable = false, name = "WORD_CREATE_DATE")
    private String createDate;

    @Column(columnDefinition = "char(1) default 'N'",name = "WORD_IS_MEMORIZE")
    private String isMemorize;

    @Column(name = "WORD_REMARKS")
    private String remarks;

}
