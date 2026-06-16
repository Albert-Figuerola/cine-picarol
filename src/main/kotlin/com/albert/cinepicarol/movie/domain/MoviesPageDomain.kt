package com.albert.cinepicarol.movie.domain

data class MoviesPageDomain (
    val movies: List<Movie>,
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalElements: Long,
    val hasPrevious: Boolean,
    val hasNext: Boolean
)