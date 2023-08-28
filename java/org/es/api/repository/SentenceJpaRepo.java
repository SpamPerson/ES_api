package org.es.api.repository;

import org.es.api.entity.Sentence;
import org.es.api.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SentenceJpaRepo extends JpaRepository<Sentence, Long> {

    List<Sentence> findAllByUserId(String userId);

    @Query(value = "CALL es_db.PRC_SELECT_SENTENCE_LIST(:USER_ID, :SEARCH_TEXT, :SEARCH_COLUMN, :CURRENT_PAGE_NUM, :PAGE_SIZE);", nativeQuery = true)
    List<Object[]> prcListSentence(
            @Param("USER_ID") String userId,
            @Param("SEARCH_TEXT") String searchText,
            @Param("SEARCH_COLUMN") String searchColumn,
            @Param("CURRENT_PAGE_NUM") int currentPageNum,
            @Param("PAGE_SIZE") int pageSize
    );

    @Query(value = "CALL es_db.PRC_COUNT_SENTENCE_LIST(:USER_ID, :SEARCH_TEXT, :SEARCH_COLUMN);", nativeQuery = true)
    List<Object[]> prcCountSentence(
            @Param("USER_ID") String userId,
            @Param("SEARCH_TEXT") String searchText,
            @Param("SEARCH_COLUMN") String searchColumn
    );
}
