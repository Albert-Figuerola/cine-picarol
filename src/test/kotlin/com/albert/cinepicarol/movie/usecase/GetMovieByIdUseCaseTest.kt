package com.albert.cinepicarol.movie.usecase

import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.port.MoviePort
import com.albert.cinepicarol.movie.query.usecase.GetMovieByIdUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals

class GetMovieByIdUseCaseTest {

    private val moviePort = mock<MoviePort>()
    private val getMovieByIdUseCase = GetMovieByIdUseCase(moviePort)

    @Test
    fun `should return movie when movie exists`() {
        val movie = createMovie()

        whenever(moviePort.findById(movie.id))
            .thenReturn(movie)

        val result = getMovieByIdUseCase.execute(movie.id)

        assertEquals(movie.id, result.id)

        verify(moviePort)
            .findById(movie.id)
    }

    @Test
    fun `should throw exception when movie does not exist`() {
        val movieId = UUID.randomUUID()

        whenever(moviePort.findById(movieId))
            .thenReturn(null)

        assertThrows<MovieNotFoundException> {
            getMovieByIdUseCase.execute(movieId)
        }

        verify(moviePort)
            .findById(movieId)
    }

    private fun createMovie(): Movie {
        return Movie(
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