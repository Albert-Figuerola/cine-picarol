package com.albert.cinepicarol.movie.query.response

import java.util.UUID

data class MovieResponse (
    val id: UUID,
    val title: String,
    val description: String,
    val releaseYear: Int?,
    val durationMinutes: Int
)