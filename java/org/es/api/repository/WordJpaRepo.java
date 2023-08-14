package org.es.api.repository;

import org.es.api.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordJpaRepo extends JpaRepository<Word, Long> {
    List<Word> findAllByUserId(String userId);
}
