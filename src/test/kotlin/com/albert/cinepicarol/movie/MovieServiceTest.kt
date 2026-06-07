package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.request.CreateMovieRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import kotlin.test.assertEquals
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

class MovieServiceTest {

    private val movieRepository = mock<MovieRepository>()
    private val movieService = MovieService(movieRepository)

    @Test
    fun `should create movie`() {
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
    fun `should throw exception when title is empty`() {
        val movie = createMovieRequest(title = "")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun `should throw exception when title contains only spaces`() {
        val movie = createMovieRequest(title = "     ")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun `should throw exception when description is empty`() {
        val movie = createMovieRequest(description = "")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun `should throw exception when description contains only spaces`() {
        val movie = createMovieRequest(description = "     ")

        assertThrows<IllegalArgumentException> {
            movieService.createMovie(movie)
        }
    }

    @Test
    fun `should throw exception when duration is zero`() {
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

}