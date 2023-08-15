package org.es.api.repository;

import org.es.api.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface WordJpaRepo extends JpaRepository<Word, Long> {
    List<Word> findAllByUserId(String userId);

    @Query(value = "CALL es_db.PRC_SELECT_WORD_LIST(:USER_ID, :SEARCH_TEXT, :SEARCH_COLUMN, :ORDER_BY);", nativeQuery = true)
    List<Object[]> prcListWord(
            @Param("USER_ID") String userId,
            @Param("SEARCH_TEXT") String search_text,
            @Param("SEARCH_COLUMN") String searchColumn,
            @Param("ORDER_BY") String orderBy
    );
}
