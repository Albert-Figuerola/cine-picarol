package com.albert.cinepicarol.movie.usecase

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.request.CreateMovieRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class CreateMovieUseCase(
    private val movieRepository: MovieRepository
) {

    fun execute(request: CreateMovieRequest): MovieEntity {
        require(request.title.isNotBlank()) {
            "Movie title cannot be empty"
        }

        require(request.description.isNotBlank()) {
            "Movie description cannot be empty"
        }

        require(request.durationMinutes > 0) {
            "Movie duration minutes must be greater than zero"
        }

        val movieEntity = MovieEntity(
            id = UUID.randomUUID(),
            title = request.title,
            description = request.description,
            releaseYear = request.releaseYear,
            durationMinutes = request.durationMinutes,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return movieRepository.save(movieEntity)
    }

}