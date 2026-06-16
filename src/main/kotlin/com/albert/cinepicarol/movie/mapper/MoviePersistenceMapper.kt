package com.albert.cinepicarol.movie.mapper

import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.entity.MovieEntity

internal fun Movie.toEntity(): MovieEntity =
    MovieEntity(
        id = id,
        title = title,
        description = description,
        releaseYear = releaseYear,
        durationMinutes = durationMinutes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )