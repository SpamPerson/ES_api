package org.es.api.repository;

import org.es.api.entity.Word;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface WordJpaRepo extends JpaRepository<Word, Long> {
    List<Word> findAllByUserId(String userId);

    Optional<Word> findByKrWord(String krWord);

    Optional<Word> findByEnWord(String enWord);

    Page<Word> findByEnWordContaining(String searchValue, Pageable pageable);

    Page<Word> findByKrWordContaining(String searchValue, Pageable pageable);

    Page<Word> findByRemarksContaining(String searchValue, Pageable pageable);

    @Query(value = "CALL es_db.PRC_SELECT_WORD_LIST(:USER_ID, :SEARCH_TEXT, :SEARCH_COLUMN, :CURRENT_PAGE_NUM, :PAGE_SIZE);", nativeQuery = true)
    List<Object[]> prcListWord(
            @Param("USER_ID") String userId,
            @Param("SEARCH_TEXT") String searchText,
            @Param("SEARCH_COLUMN") String searchColumn,
            @Param("CURRENT_PAGE_NUM") int currentPageNum,
            @Param("PAGE_SIZE") int pageSize
    );

    @Query(value = "CALL es_db.PRC_COUNT_WORD_LIST(:USER_ID, :SEARCH_TEXT, :SEARCH_COLUMN)",nativeQuery = true)
    List<Object[]> prcCountWordList(
            @Param("USER_ID") String userId,
            @Param("SEARCH_TEXT") String searchText,
            @Param("SEARCH_COLUMN") String searchColumn
    );

    @Query(value = "CALL es_db.PRC_UPDATE_WORD(:WORD_CODE, :COLUMN_NAME, :VALUE);", nativeQuery = true)
    List<Object[]> prcUpdateWord(@Param("WORD_CODE") String wordCode, @Param("COLUMN_NAME") String columnName, @Param("VALUE") String value);

    @Query(value = "CALL es_db.PRC_SELECT_WORD_BY_KR_WORD(:USER_ID, :WORD);", nativeQuery = true)
    List<Object[]> prcSelectWordByKrWord(@Param("USER_ID") String userId, @Param("WORD") String word);

    @Query(value = "CALL es_db.PRC_SELECT_WORD_BY_EN_WORD(:USER_ID, :WORD);", nativeQuery = true)
    List<Object[]> prcSelectWordByEnWord(@Param("USER_ID") String userId, @Param("WORD") String word);

}
