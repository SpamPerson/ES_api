package org.es.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_USER_ROLE")
public class UserRole {
    @Id @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "ROLE_CODE")
    private UUID roleCode;
    @Column(length = 100, nullable = false, name = "ROLE_NAME")
    private String name;
    @Column(length = 255, nullable = true, name = "ROLE_DESC")
    private String description;
}
