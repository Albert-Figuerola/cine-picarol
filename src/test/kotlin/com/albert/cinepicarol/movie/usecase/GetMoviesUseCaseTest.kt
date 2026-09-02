package com.albert.cinepicarol.movie.usecase

import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.domain.MoviesPageDomain
import com.albert.cinepicarol.movie.port.MoviePort
import com.albert.cinepicarol.movie.query.usecase.GetMoviesUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.UUID

class GetMoviesUseCaseTest {

    private val moviePort = mock<MoviePort>()
    private val getMoviesUseCase = GetMoviesUseCase(moviePort)

    @Test
    fun `should return paginated movies`() {
        val pageable = PageRequest.of(0, 10)

        val movies = listOf(
            createMovie(title = "Titanic"),
            createMovie(title = "Interstellar")
        )

        val page = MoviesPageDomain(movies, 0, 10, 1, 2, false, false)

        whenever(moviePort.findAll(pageable))
            .thenReturn(page)

        val result = getMoviesUseCase.execute(pageable)

        assertEquals(2, result.totalElements)
        assertEquals("Titanic", result.movies.first().title)
        assertEquals("Interstellar", result.movies[1].title)
        assertEquals(2, result.totalElements)

        verify(moviePort).findAll(pageable)
    }

    private fun createMovie(
        title: String = "Title test"
    ): Movie {
        return Movie(
            id = UUID.randomUUID(),
            title = title,
            description = "Description test",
            releaseYear = 1997,
            durationMinutes = 194,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

}