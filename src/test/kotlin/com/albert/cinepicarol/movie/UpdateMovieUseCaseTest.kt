package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.command.request.UpdateMovieRequest
import com.albert.cinepicarol.movie.command.usecase.UpdateMovieUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.never
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals

class UpdateMovieUseCaseTest {

    private val movieRepository = mock<MovieRepository>()
    private val updateMovieUseCase = UpdateMovieUseCase(movieRepository)

    @Test
    fun `should update title`() {
        val movie = createMovie()
        val request = updateMovieRequest(title = "Interstellar updated")

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.of(movie))

        whenever(movieRepository.save(any<MovieEntity>()))
            .thenAnswer { it.arguments[0] as MovieEntity }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals("Interstellar updated", result.title)
        assertEquals(movie.description, result.description)
        assertEquals(movie.releaseYear, result.releaseYear)
        assertEquals(movie.durationMinutes, result.durationMinutes)

        verify(movieRepository)
            .findById(movie.id)

        verify(movieRepository)
            .save(movie)
    }

    @Test
    fun `should update description`() {
        val movie = createMovie()
        val request = updateMovieRequest(description = "Interstellar updated")

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.of(movie))

        whenever(movieRepository.save(any<MovieEntity>()))
            .thenAnswer { it.arguments[0] as MovieEntity }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals(movie.title, result.title)
        assertEquals("Interstellar updated", result.description)
        assertEquals(movie.releaseYear, result.releaseYear)
        assertEquals(movie.durationMinutes, result.durationMinutes)

        verify(movieRepository)
            .findById(movie.id)

        verify(movieRepository)
            .save(movie)
    }

    @Test
    fun `should update release year`() {
        val movie = createMovie()
        val request = updateMovieRequest(releaseYear = 2017)

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.of(movie))

        whenever(movieRepository.save(any<MovieEntity>()))
            .thenAnswer { it.arguments[0] as MovieEntity }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals(movie.title, result.title)
        assertEquals(movie.description, result.description)
        assertEquals(2017, result.releaseYear)
        assertEquals(movie.durationMinutes, result.durationMinutes)

        verify(movieRepository)
            .findById(movie.id)

        verify(movieRepository)
            .save(movie)
    }

    @Test
    fun `should update duration`() {
        val movie = createMovie()
        val request = updateMovieRequest(durationMinutes = 197)

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.of(movie))

        whenever(movieRepository.save(any<MovieEntity>()))
            .thenAnswer { it.arguments[0] as MovieEntity }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals(movie.title, result.title)
        assertEquals(movie.description, result.description)
        assertEquals(movie.releaseYear, result.releaseYear)
        assertEquals(197, result.durationMinutes)

        verify(movieRepository)
            .findById(movie.id)

        verify(movieRepository)
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

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.of(movie))

        whenever(movieRepository.save(any<MovieEntity>()))
            .thenAnswer { it.arguments[0] as MovieEntity }

        val result = updateMovieUseCase.execute(movie.id, request)

        assertEquals("Interstellar updated", result.title)
        assertEquals("Interstellar updated", result.description)
        assertEquals(2017, result.releaseYear)
        assertEquals(197, result.durationMinutes)

        verify(movieRepository)
            .findById(movie.id)

        verify(movieRepository)
            .save(movie)
    }

    @Test
    fun `should throw exception when movie does not exist`() {
        val movieId = UUID.randomUUID()

        val request = updateMovieRequest(
            title = "Interstellar updated"
        )

        whenever(movieRepository.findById(movieId))
            .thenReturn(Optional.empty())

        val exception = assertThrows<MovieNotFoundException> {
            updateMovieUseCase.execute(movieId, request)
        }

        assertEquals(
            "Movie with id $movieId not found",
            exception.message
        )

        verify(movieRepository)
            .findById(movieId)

        verify(movieRepository, never())
            .save(any<MovieEntity>())
    }

    @Test
    fun `should throw exception when title is empty`() {
        val movie = createMovie()
        val request = updateMovieRequest(
            title = "",
        )

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.of(movie))

        whenever(movieRepository.save(any<MovieEntity>()))
            .thenAnswer { it.arguments[0] as MovieEntity }

        val exception = assertThrows<IllegalArgumentException> {
            updateMovieUseCase.execute(movie.id, request)
        }

        assertEquals(
            "Movie title cannot be empty",
            exception.message
        )

        verify(movieRepository)
            .findById(movie.id)

        verify(movieRepository, never())
            .save(any<MovieEntity>())
    }

    @Test
    fun `should throw exception when duration is zero`() {
        val movie = createMovie()
        val request = updateMovieRequest(
            durationMinutes = 0,
        )

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.of(movie))

        whenever(movieRepository.save(any<MovieEntity>()))
            .thenAnswer { it.arguments[0] as MovieEntity }

        val exception = assertThrows<IllegalArgumentException> {
            updateMovieUseCase.execute(movie.id, request)
        }

        assertEquals(
            "Movie duration minutes must be greater than zero",
            exception.message
        )

        verify(movieRepository)
            .findById(movie.id)

        verify(movieRepository, never())
            .save(any<MovieEntity>())
    }

    private fun createMovie(
        title: String = "Titanic",
        description: String = "Titanic description",
        releaseYear: Int? = 1997,
        durationMinutes: Int = 194
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