package com.albert.cinepicarol.movie.usecase

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.request.UpdateMovieRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdateMovieUseCase(
    private val movieRepository: MovieRepository
) {

    fun execute(id: UUID, request: UpdateMovieRequest): MovieEntity {
        val movie = movieRepository.findByIdOrNull(id) ?: throw MovieNotFoundException(id)

        request.title?.let {
            require(it.isNotBlank()) {
                "Movie title cannot be empty"
            }

            movie.title = it
        }

        request.description?.let {
            require(it.isNotBlank()) {
                "Movie description cannot be empty"
            }

            movie.description = it
        }

        request.releaseYear?.let {
            require(it > 0) {
                "Movie release year must be greater than zero"
            }

            movie.releaseYear = it
        }

        request.durationMinutes?.let {
            require(it > 0) {
                "Movie duration minutes must be greater than zero"
            }

            movie.durationMinutes = it
        }

        return movieRepository.save(movie)
    }

}