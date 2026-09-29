package com.albert.cinepicarol.movie.domain

import java.util.UUID

data class Movie (
    val id: UUID,
    var title: String,
    var description: String,
    var releaseYear: Int?,
    var durationMinutes: Int
)