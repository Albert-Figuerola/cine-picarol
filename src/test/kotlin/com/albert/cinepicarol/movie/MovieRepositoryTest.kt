package com.albert.cinepicarol.movie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
class MovieRepositoryTest {

    @Autowired
    private lateinit var movieRepository: MovieRepository

    @Test
    fun shouldSaveMovie() {

        val movie = MovieEntity(
            id = UUID.randomUUID(),
            title = "Test Movie",
            description = "Test Description",
            releaseYear = 2023,
            durationMinutes = 120,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val savedMovie = movieRepository.save(movie)

        val foundMovie = movieRepository.findByIdOrNull(savedMovie.id)

        assertNotNull(foundMovie)
        assertEquals(movie.id, foundMovie.id)
        assertEquals(movie.title, foundMovie.title)
        assertEquals(movie.description, foundMovie.description)
        assertEquals(movie.releaseYear, foundMovie.releaseYear)
    }

}