package com.albert.cinepicarol.movie.command.model

data class CreateMovieCommand (
    val title: String,
    val description: String,
    val releaseYear: Int?,
    val durationMinutes: Int
)