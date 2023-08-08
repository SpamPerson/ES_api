package org.es.api.repository;

import org.es.api.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(String userId);
}
