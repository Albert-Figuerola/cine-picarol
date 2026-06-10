package com.albert.cinepicarol.movie.command.usecase

import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.repository.MovieRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DeleteMovieUseCase(
    private val movieRepository: MovieRepository
) {

    fun execute(id: UUID)  {
        val movie = movieRepository.findByIdOrNull(id) ?: throw MovieNotFoundException(id)
        movieRepository.delete(movie)
    }

}