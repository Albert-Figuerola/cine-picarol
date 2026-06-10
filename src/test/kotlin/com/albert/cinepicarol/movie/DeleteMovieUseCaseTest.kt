package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.command.usecase.DeleteMovieUseCase
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

class DeleteMovieUseCaseTest {

    private val movieRepository = mock<MovieRepository>()
    private val deleteMovieUseCase = DeleteMovieUseCase(movieRepository)

    @Test
    fun `should delete movie`() {
        val movie = createMovie()

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.of(movie))

        deleteMovieUseCase.execute(movie.id)

        verify(movieRepository)
            .delete(movie)
    }

    @Test
    fun `should throw exception when movie does not exist`() {
        val movie = createMovie()

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.empty())

        val exception = assertThrows<MovieNotFoundException> {
            deleteMovieUseCase.execute(movie.id)
        }

        assertEquals(
            "Movie with id ${movie.id} not found",
            exception.message
        )

        verify(movieRepository, never())
            .delete(any())
    }

    private fun createMovie(): MovieEntity {
        return MovieEntity(
            id = UUID.randomUUID(),
            title = "Titanic",
            description = "Titanic description",
            releaseYear = 1997,
            durationMinutes = 194,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

}