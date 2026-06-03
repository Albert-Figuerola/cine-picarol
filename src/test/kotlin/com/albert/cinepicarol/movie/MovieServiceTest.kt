package com.albert.cinepicarol.movie

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    @Test
    fun shouldThrowExceptionWhenTitleIsEmpty() {
        val movie = createMovie(title = "")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun shouldThrowExceptionWhenTitleContainsOnlySpaces() {
        val movie = createMovie(title = "     ")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun shouldThrowExceptionWhenDescriptionIsEmpty() {
        val movie = createMovie(description = "")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun shouldThrowExceptionWhenDescriptionContainsOnlySpaces() {
        val movie = createMovie(description = "     ")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun shouldThrowExceptionWhenDurationIsZero() {
        val movie = createMovie(durationMinutes = 0)

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    private fun createMovie(
        title: String = "Title test",
        description: String = "Description test",
        releaseYear: Int? = 2023,
        durationMinutes: Int = 120
    ): MovieEntity {
        return MovieEntity(
            id = UUID.randomUUID(),
            title = title,
            description = description,
            releaseYear = releaseYear,
            durationMinutes = durationMinutes,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

}