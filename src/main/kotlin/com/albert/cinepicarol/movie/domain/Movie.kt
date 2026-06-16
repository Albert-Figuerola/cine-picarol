package com.albert.cinepicarol.movie.domain

import java.time.LocalDateTime
import java.util.UUID

data class Movie (
    val id: UUID,
    var title: String,
    var description: String,
    var releaseYear: Int?,
    var durationMinutes: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)