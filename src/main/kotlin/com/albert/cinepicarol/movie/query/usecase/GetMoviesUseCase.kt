package com.albert.cinepicarol.movie.query.usecase

import com.albert.cinepicarol.movie.domain.MoviesPageDomain
import com.albert.cinepicarol.movie.port.MoviePort
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GetMoviesUseCase (
    private val moviePort: MoviePort
) {

    fun execute(pageable: Pageable) : MoviesPageDomain {
        return moviePort.findAll(pageable)
    }

}