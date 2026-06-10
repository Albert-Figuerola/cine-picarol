package com.albert.cinepicarol.movie.command.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class CreateMovieRequest (
    @field:NotBlank(
        message = "Movie title cannot be empty"
    )
    val title: String,

    @field:NotBlank(
        message = "Movie description cannot be empty"
    )
    val description: String,

    val releaseYear: Int?,

    @field:Positive(
        message = "Movie duration minutes must be greater than zero"
    )
    val durationMinutes: Int
)