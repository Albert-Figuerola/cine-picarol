package com.albert.cinepicarol.movie.adapter

import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.domain.MoviesPageDomain
import com.albert.cinepicarol.movie.mapper.toDomain
import com.albert.cinepicarol.movie.mapper.toEntity
import com.albert.cinepicarol.movie.port.MoviePort
import com.albert.cinepicarol.movie.repository.MovieRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MovieJpaAdapter (
    private val movieRepository: MovieRepository
) : MoviePort {

    override fun save(movie: Movie): Movie {
        return movieRepository.save(movie.toEntity()).toDomain()
    }

    override fun findById(id: UUID): Movie? {
        return movieRepository.findByIdOrNull(id)?.toDomain()
    }

    override fun findAll(pageable: Pageable): MoviesPageDomain {
        return movieRepository.findAll(pageable).toDomain()
    }

    override fun deleteById(id: UUID) {
        movieRepository.deleteById(id)
    }

}