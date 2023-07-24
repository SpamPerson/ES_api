package org.es.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_USER_ROLE")
public class UserRole {
    @Id
    @Column(name = "ROLE_CODE")
    private UUID roleCode;
    @Column(length = 100, nullable = false, name = "ROLE_NAME")
    private String name;
    @Column(length = 255, nullable = true, name = "ROLE_DESC")
    private String description;
}
