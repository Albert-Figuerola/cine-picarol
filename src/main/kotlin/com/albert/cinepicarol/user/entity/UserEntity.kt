package com.albert.cinepicarol.user.entity

import com.albert.cinepicarol.user.domain.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,

    @Column(name = "first_name")
    val firstName: String,

    @Column(name = "last_name")
    val lastName: String,

    val email: String,

    @Column(name = "password_hash")
    val passwordHash: String,

    @Enumerated(EnumType.STRING)
    val role: UserRole,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime

) {
}