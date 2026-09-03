package com.albert.cinepicarol.movie.command.usecase

import com.albert.cinepicarol.movie.command.model.CreateMovieCommand
import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.port.MoviePort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class CreateMovieUseCase(
    private val moviePort: MoviePort
) {

    fun execute(request: CreateMovieCommand): Movie {

        val movie = Movie(
            id = UUID.randomUUID(),
            title = request.title,
            description = request.description,
            releaseYear = request.releaseYear,
            durationMinutes = request.durationMinutes,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return moviePort.save(movie)
    }

}