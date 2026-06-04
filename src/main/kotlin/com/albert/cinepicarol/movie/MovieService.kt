package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.request.CreateMovieRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class MovieService (
    private val movieRepository: MovieRepository
) {

    fun createMovie(request: CreateMovieRequest): MovieEntity  {
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

    fun getMovieById(id: UUID): MovieEntity =
        movieRepository.findByIdOrNull(id) ?: throw MovieNotFoundException(id)

    fun getMovies(): List<MovieEntity> =
        movieRepository.findAll()

}