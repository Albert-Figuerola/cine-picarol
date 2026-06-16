package com.albert.cinepicarol.movie.query.usecase

import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.mapper.toDomain
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetMovieByIdUseCase (
    private val movieRepository: MovieRepository
) {

    fun execute(id: UUID): Movie {
        val movie = movieRepository.findByIdOrNull(id)
            ?: throw MovieNotFoundException(id)

        return movie.toDomain()
    }

}