package com.albert.cinepicarol.movie.query.usecase

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetMovieByIdUseCase (
    private val movieRepository: MovieRepository
) {

    fun execute(id: UUID): MovieEntity {
        val movie = movieRepository.findByIdOrNull(id)
            ?: throw MovieNotFoundException(id)

        return movie
    }

}