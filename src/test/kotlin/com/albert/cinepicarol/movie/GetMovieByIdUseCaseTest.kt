package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.usecase.GetMovieByIdUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID
import kotlin.test.assertEquals

class GetMovieByIdUseCaseTest {

    private val movieRepository = mock<MovieRepository>()
    private val getMovieByIdUseCase = GetMovieByIdUseCase(movieRepository)

    @Test
    fun `should return movie when movie exists`() {
        val movie = createMovie()

        whenever(movieRepository.findById(movie.id))
            .thenReturn(Optional.of(movie))

        val result = getMovieByIdUseCase.execute(movie.id)

        assertEquals(movie.id, result.id)

        verify(movieRepository)
            .findById(movie.id)
    }

    @Test
    fun `should throw exception when movie does not exist`() {
        val movieId = UUID.randomUUID()

        whenever(movieRepository.findById(movieId))
            .thenReturn(Optional.empty())

        assertThrows<MovieNotFoundException> {
            getMovieByIdUseCase.execute(movieId)
        }

        verify(movieRepository)
            .findById(movieId)
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