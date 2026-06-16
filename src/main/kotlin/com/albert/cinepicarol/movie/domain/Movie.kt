package com.albert.cinepicarol.movie.domain

import java.time.LocalDateTime
import java.util.UUID

data class Movie (
    val id: UUID,
    val title: String,
    val description: String,
    val releaseYear: Int?,
    val durationMinutes: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)