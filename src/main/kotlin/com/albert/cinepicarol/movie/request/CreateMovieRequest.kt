package com.albert.cinepicarol.movie.request

data class CreateMovieRequest (
    val title: String,
    val description: String,
    val releaseYear: Int?,
    val durationMinutes: Int
)