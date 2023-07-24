package org.es.api.repository;

import org.es.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepo extends JpaRepository<User, UUID> {
    Optional<User> findByUserId(String userId);
}
