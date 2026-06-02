package com.albert.cinepicarol.movie

import org.springframework.stereotype.Service

@Service
class MovieService (
    private val movieRepository: MovieRepository
) {

    fun createMovie(movie: MovieEntity): MovieEntity  = movieRepository.save(movie)

}