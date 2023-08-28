package org.es.api.repository;

import org.es.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepo extends JpaRepository<User, UUID> {
    Optional<User> findByUserId(String userId);

    Optional<User> findByMail(String mail);

    List<User> findAllByUserIdIn(List<String> userIds);

    Page<User> findByUserIdContaining(String searchValue, Pageable pageable);

    Page<User> findByNameContaining(String searchValue, Pageable pageable);

    Page<User> findByMailContaining(String searchValue, Pageable pageable);
}
