package com.albert.cinepicarol.movie.mapper

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.response.MovieResponse
import com.albert.cinepicarol.movie.response.MoviesPageResponse
import org.springframework.data.domain.Page

internal fun MovieEntity.toResponse() = MovieResponse(
    id,
    title,
    description,
    releaseYear,
    durationMinutes
)


internal fun Page<MovieEntity>.toResponse(): MoviesPageResponse =
    MoviesPageResponse(
        movies = content.map { it.toResponse() },
        currentPage = number,
        pageSize = size,
        totalPages = totalPages,
        totalElements = totalElements,
        hasPrevious = hasPrevious(),
        hasNext = hasNext()
    )