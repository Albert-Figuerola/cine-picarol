package com.albert.cinepicarol.movie.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "movie")
class MovieEntity (

    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var description: String,

    @Column(name = "release_year")
    var releaseYear: Int?,

    @Column(nullable = false)
    var durationMinutes: Int,

    @Column(nullable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = false)
    val updatedAt: LocalDateTime

)