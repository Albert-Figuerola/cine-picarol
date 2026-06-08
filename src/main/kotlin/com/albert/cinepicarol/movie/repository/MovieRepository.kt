package com.albert.cinepicarol.movie.repository

import com.albert.cinepicarol.movie.entity.MovieEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MovieRepository : JpaRepository<MovieEntity, UUID> {

}