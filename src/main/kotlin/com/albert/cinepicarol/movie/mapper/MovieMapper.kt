package com.albert.cinepicarol.movie.mapper

import com.albert.cinepicarol.movie.MovieEntity
import com.albert.cinepicarol.movie.response.MovieResponse

internal fun MovieEntity.toResponse() = MovieResponse(
        id,
        title,
        description,
        releaseYear,
        durationMinutes
)

internal fun List<MovieEntity>.toResponse(): List<MovieResponse> =
        map { it.toResponse() }