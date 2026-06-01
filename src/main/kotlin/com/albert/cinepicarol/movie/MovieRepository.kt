package com.albert.cinepicarol.movie

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MovieRepository : JpaRepository<MovieEntity, UUID> {

}