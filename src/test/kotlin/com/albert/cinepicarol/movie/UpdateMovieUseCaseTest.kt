package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.command.request.UpdateMovieRequest
import com.albert.cinepicarol.movie.command.usecase.UpdateMovieUseCase
import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.port.MoviePort
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.never
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

class UpdateMovieUseCaseTest {

    private val moviePort = mock<MoviePort>()
    private val updateMovieUseCase = UpdateMovieUseCase(moviePort)

    @Test
    fun `should update title`() {
        val movie = createMovie()
        val request = updateMovieRequest(title = "Interstellar updated")

        whenever(moviePort.findById(movie.id))
            .thenReturn(movie)

        whenever(moviePort.save(any<Movie>()))
            .thenAnswer { it.arguments[0] as Movie }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals("Interstellar updated", result.title)
        assertEquals(movie.description, result.description)
        assertEquals(movie.releaseYear, result.releaseYear)
        assertEquals(movie.durationMinutes, result.durationMinutes)

        verify(moviePort)
            .findById(movie.id)

        verify(moviePort)
            .save(movie)
    }

    @Test
    fun `should update description`() {
        val movie = createMovie()
        val request = updateMovieRequest(description = "Interstellar updated")

        whenever(moviePort.findById(movie.id))
            .thenReturn(movie)

        whenever(moviePort.save(any<Movie>()))
            .thenAnswer { it.arguments[0] as Movie }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals(movie.title, result.title)
        assertEquals("Interstellar updated", result.description)
        assertEquals(movie.releaseYear, result.releaseYear)
        assertEquals(movie.durationMinutes, result.durationMinutes)

        verify(moviePort)
            .findById(movie.id)

        verify(moviePort)
            .save(movie)
    }

    @Test
    fun `should update release year`() {
        val movie = createMovie()
        val request = updateMovieRequest(releaseYear = 2017)

        whenever(moviePort.findById(movie.id))
            .thenReturn(movie)

        whenever(moviePort.save(any<Movie>()))
            .thenAnswer { it.arguments[0] as Movie }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals(movie.title, result.title)
        assertEquals(movie.description, result.description)
        assertEquals(2017, result.releaseYear)
        assertEquals(movie.durationMinutes, result.durationMinutes)

        verify(moviePort)
            .findById(movie.id)

        verify(moviePort)
            .save(movie)
    }

    @Test
    fun `should update duration`() {
        val movie = createMovie()
        val request = updateMovieRequest(durationMinutes = 197)

        whenever(moviePort.findById(movie.id))
            .thenReturn(movie)

        whenever(moviePort.save(any<Movie>()))
            .thenAnswer { it.arguments[0] as Movie }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals(movie.title, result.title)
        assertEquals(movie.description, result.description)
        assertEquals(movie.releaseYear, result.releaseYear)
        assertEquals(197, result.durationMinutes)

        verify(moviePort)
            .findById(movie.id)

        verify(moviePort)
            .save(movie)
    }

    @Test
    fun `should update multiple fields`() {
        val movie = createMovie()
        val request = updateMovieRequest(
            title = "Interstellar updated",
            description = "Interstellar updated",
            releaseYear = 2017,
            durationMinutes = 197
        )

        whenever(moviePort.findById(movie.id))
            .thenReturn(movie)

        whenever(moviePort.save(any<Movie>()))
            .thenAnswer { it.arguments[0] as Movie }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals("Interstellar updated", result.title)
        assertEquals("Interstellar updated", result.description)
        assertEquals(2017, result.releaseYear)
        assertEquals(197, result.durationMinutes)

        verify(moviePort)
            .findById(movie.id)

        verify(moviePort)
            .save(movie)
    }

    @Test
    fun `should throw exception when movie does not exist`() {
        val movieId = UUID.randomUUID()

        val request = updateMovieRequest(
            title = "Interstellar updated"
        )

        whenever(moviePort.findById(movieId))
            .thenReturn(null)

        val exception = assertThrows<MovieNotFoundException> {
            updateMovieUseCase.execute(movieId, request)
        }

        assertEquals(
            "Movie with id $movieId not found",
            exception.message
        )

        verify(moviePort)
            .findById(movieId)

        verify(moviePort, never())
            .save(any<Movie>())
    }

    @Test
    fun `should throw exception when title is empty`() {
        val movie = createMovie()
        val request = updateMovieRequest(
            title = "",
        )

        whenever(moviePort.findById(movie.id))
            .thenReturn(movie)

        whenever(moviePort.save(any<Movie>()))
            .thenAnswer { it.arguments[0] as Movie }

        val exception = assertThrows<IllegalArgumentException> {
            updateMovieUseCase.execute(movie.id, request)
        }

        assertEquals(
            "Movie title cannot be empty",
            exception.message
        )

        verify(moviePort)
            .findById(movie.id)

        verify(moviePort, never())
            .save(any<Movie>())
    }

    @Test
    fun `should throw exception when duration is zero`() {
        val movie = createMovie()
        val request = updateMovieRequest(
            durationMinutes = 0,
        )

        whenever(moviePort.findById(movie.id))
            .thenReturn(movie)

        whenever(moviePort.save(any<Movie>()))
            .thenAnswer { it.arguments[0] as Movie }

        val exception = assertThrows<IllegalArgumentException> {
            updateMovieUseCase.execute(movie.id, request)
        }

        assertEquals(
            "Movie duration minutes must be greater than zero",
            exception.message
        )

        verify(moviePort)
            .findById(movie.id)

        verify(moviePort, never())
            .save(any<Movie>())
    }

    private fun createMovie(
        title: String = "Titanic",
        description: String = "Titanic description",
        releaseYear: Int? = 1997,
        durationMinutes: Int = 194
    ): Movie {
        return Movie(
            id = UUID.randomUUID(),
            title = title,
            description = description,
            releaseYear = releaseYear,
            durationMinutes = durationMinutes,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    private fun updateMovieRequest(
        title: String? = null,
        description: String? = null,
        releaseYear: Int? = null,
        durationMinutes: Int? = null
    ): UpdateMovieRequest {
        return UpdateMovieRequest(
            title = title,
            description = description,
            releaseYear = releaseYear,
            durationMinutes = durationMinutes
        )
    }

}