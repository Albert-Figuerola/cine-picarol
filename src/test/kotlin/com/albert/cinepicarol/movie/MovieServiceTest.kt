package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.request.CreateMovieRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
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
        val request = createMovieRequest()

        whenever(movieRepository.save(any<MovieEntity>()))
            .thenAnswer { it.arguments[0] as MovieEntity }

        val result = movieService.createMovie(request)

        assertEquals(request.title, result.title)
        assertEquals(request.description, result.description)
        assertEquals(request.releaseYear, result.releaseYear)
        assertEquals(request.durationMinutes, result.durationMinutes)

        verify(movieRepository).save(any<MovieEntity>())
    }

    @Test
    fun shouldThrowExceptionWhenTitleIsEmpty() {
        val movie = createMovieRequest(title = "")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun shouldThrowExceptionWhenTitleContainsOnlySpaces() {
        val movie = createMovieRequest(title = "     ")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun shouldThrowExceptionWhenDescriptionIsEmpty() {
        val movie = createMovieRequest(description = "")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun shouldThrowExceptionWhenDescriptionContainsOnlySpaces() {
        val movie = createMovieRequest(description = "     ")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun shouldThrowExceptionWhenDurationIsZero() {
        val movie = createMovieRequest(durationMinutes = 0)

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    private fun createMovieRequest(
        title: String = "Title test",
        description: String = "Description test",
        releaseYear: Int? = 2023,
        durationMinutes: Int = 120
    ): CreateMovieRequest {
        return CreateMovieRequest(
            title = title,
            description = description,
            releaseYear = releaseYear,
            durationMinutes = durationMinutes
        )
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