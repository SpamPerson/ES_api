package org.es.api.repository;

import org.es.api.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SentenceJpaRepo extends JpaRepository<Sentence, Long> {
    @Query(value = "CALL es_db.PRC_SELECT_SENTENCE_LIST(:USER_ID, :SEARCH_TEXT, :SEARCH_COLUMN, :ORDER_BY);", nativeQuery = true)
    List<Object[]> prcListSentence(
            @Param("USER_ID") String userId,
            @Param("SEARCH_TEXT") String searchText,
            @Param("SEARCH_COLUMN") String searchColumn,
            @Param("ORDER_BY") String orderBy
    );
}
