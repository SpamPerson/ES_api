package org.es.api.repository;

import org.es.api.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRoleJpaRepo extends JpaRepository<UserRole, UUID> {
    Optional<UserRole> findByName(String name);
}
