package com.albert.cinepicarol.movie.command.usecase

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.command.request.CreateMovieRequest
import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.mapper.toDomain
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class CreateMovieUseCase(
    private val movieRepository: MovieRepository
) {

    fun execute(request: CreateMovieRequest): Movie {

        val movieEntity = MovieEntity(
            id = UUID.randomUUID(),
            title = request.title,
            description = request.description,
            releaseYear = request.releaseYear,
            durationMinutes = request.durationMinutes,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return movieRepository.save(movieEntity).toDomain()
    }

}