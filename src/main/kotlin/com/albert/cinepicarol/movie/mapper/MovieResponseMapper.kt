package com.albert.cinepicarol.movie.mapper

import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.domain.MoviesPageDomain
import com.albert.cinepicarol.movie.query.response.MovieResponse
import com.albert.cinepicarol.movie.query.response.MoviesPageResponse

internal fun Movie.toResponse() =
    MovieResponse(
        id = id,
        title = title,
        description = description,
        releaseYear = releaseYear,
        durationMinutes = durationMinutes
    )

internal fun MoviesPageDomain.toResponse() =
    MoviesPageResponse(
        movies = movies.map { it.toResponse() },
        currentPage = currentPage,
        pageSize = pageSize,
        totalPages = totalPages,
        totalElements = totalElements,
        hasPrevious = hasPrevious,
        hasNext = hasNext
    )