package com.albert.cinepicarol.movie.usecase

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.repository.MovieRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GetMoviesUseCase (
    private val movieRepository: MovieRepository
) {

    fun execute(pageable: Pageable) : Page<MovieEntity> {
        return movieRepository.findAll(pageable)
    }


}