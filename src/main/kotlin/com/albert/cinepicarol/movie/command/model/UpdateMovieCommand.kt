package com.albert.cinepicarol.movie.command.model

data class UpdateMovieCommand (
    val title: String?,
    val description: String?,
    val releaseYear: Int?,
    val durationMinutes: Int?
)