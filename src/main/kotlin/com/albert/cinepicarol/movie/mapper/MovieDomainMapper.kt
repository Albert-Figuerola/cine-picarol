package com.albert.cinepicarol.movie.mapper

import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.domain.MoviesPageDomain
import com.albert.cinepicarol.movie.entity.MovieEntity
import org.springframework.data.domain.Page

internal fun MovieEntity.toDomain(): Movie =
    Movie(
        id = id,
        title = title,
        description = description,
        releaseYear = releaseYear,
        durationMinutes = durationMinutes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

internal fun Page<MovieEntity>.toDomain() =
    MoviesPageDomain(
        movies = content.map { it.toDomain() },
        currentPage = number,
        pageSize = size,
        totalPages = totalPages,
        totalElements = totalElements,
        hasPrevious = hasPrevious(),
        hasNext = hasNext()
    )