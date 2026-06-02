package com.albert.cinepicarol.movie

import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

class MovieServiceTest {

    private val movieRepository = mock<MovieRepository>()
    private val movieService = MovieService(movieRepository)

    @Test
    fun shouldCreateMovie() {
        val movie = MovieEntity(
            id = UUID.randomUUID(),
            title = "Test Movie",
            description = "Test Description",
            releaseYear = 2023,
            durationMinutes = 120,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(movieRepository.save(movie))
            .thenReturn(movie)

        val result = movieService.createMovie(movie)

        assertEquals(movie.id, result.id)
        assertEquals(movie.title, result.title)

        verify(movieRepository).save(movie)
    }

}