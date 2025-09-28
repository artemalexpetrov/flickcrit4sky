package com.flickcrit.app.infrastructure.persistence.model;

import com.flickcrit.app.domain.model.user.UserRole;
import io.hypersistence.utils.hibernate.type.array.EnumArrayType;
import io.hypersistence.utils.hibernate.type.array.internal.AbstractArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.dialect.PostgreSQLArrayJdbcType;

@Entity
@Getter
@Builder
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLArrayJdbcType.class)
    @Type(value = EnumArrayType.class, parameters = @Parameter(
        name = AbstractArrayType.SQL_ARRAY_TYPE,
        value = "user_role_type"))
    private UserRole[] roles;
}
