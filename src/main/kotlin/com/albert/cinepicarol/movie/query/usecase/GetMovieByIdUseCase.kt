package com.albert.cinepicarol.movie.query.usecase

import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.port.MoviePort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetMovieByIdUseCase (
    private val moviePort: MoviePort
) {

    fun execute(id: UUID): Movie {
        val movie = moviePort.findById(id)
            ?: throw MovieNotFoundException(id)

        return movie
    }

}