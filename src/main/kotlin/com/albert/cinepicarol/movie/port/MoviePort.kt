package com.albert.cinepicarol.movie.port

import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.domain.MoviesPageDomain
import org.springframework.data.domain.Pageable
import java.util.UUID

interface MoviePort {

    fun save(movie: Movie): Movie

    fun findById(id: UUID): Movie?

    fun findAll(pageable: Pageable): MoviesPageDomain

    fun deleteById(id: UUID)

}
