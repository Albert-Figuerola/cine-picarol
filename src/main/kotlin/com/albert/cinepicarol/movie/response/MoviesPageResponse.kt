package com.albert.cinepicarol.movie.response

data class MoviesPageResponse(
    val movies: List<MovieResponse>,
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalElements: Long,
    val hasPrevious: Boolean,
    val hasNext: Boolean
)