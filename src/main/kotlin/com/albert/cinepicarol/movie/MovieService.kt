package com.albert.cinepicarol.movie

import org.springframework.stereotype.Service

@Service
class MovieService (
    private val movieRepository: MovieRepository
) {

    fun createMovie(movie: MovieEntity): MovieEntity  {
        require(movie.title.isNotBlank()) {
            "Movie title cannot be empty"
        }

        require(movie.description.isNotBlank()) {
            "Movie description cannot be empty"
        }

        require(movie.durationMinutes > 0) {
            "Movie duration minutes must be greater than zero"
        }

        return movieRepository.save(movie)
    }

}