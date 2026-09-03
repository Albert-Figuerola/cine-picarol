package com.albert.cinepicarol.movie.command.usecase

import com.albert.cinepicarol.movie.command.model.UpdateMovieCommand
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.port.MoviePort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdateMovieUseCase(
    private val moviePort: MoviePort
) {

    fun execute(id: UUID, request: UpdateMovieCommand): Movie {
        val movie = moviePort.findById(id) ?: throw MovieNotFoundException(id)

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

        return moviePort.save(movie)
    }

}