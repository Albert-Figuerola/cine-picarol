package com.albert.cinepicarol.movie.query.usecase

import com.albert.cinepicarol.movie.domain.MoviesPageDomain
import com.albert.cinepicarol.movie.mapper.toDomain
import com.albert.cinepicarol.movie.repository.MovieRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GetMoviesUseCase (
    private val movieRepository: MovieRepository
) {

    fun execute(pageable: Pageable) : MoviesPageDomain {
        return movieRepository.findAll(pageable).toDomain()
    }

}