package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.command.request.CreateMovieRequest
import com.albert.cinepicarol.movie.command.usecase.CreateMovieUseCase
import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.port.MoviePort
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class CreateMovieUseCaseTest {

    private val moviePort = mock<MoviePort>()
    private val createMovieUseCase = CreateMovieUseCase(moviePort)

    @Test
    fun `should create movie`() {
        val request = createMovieRequest()

        whenever(moviePort.save(any<Movie>()))
            .thenAnswer { it.arguments[0] as Movie }

        val result = createMovieUseCase.execute(request)

        assertEquals(request.title, result.title)
        assertEquals(request.description, result.description)
        assertEquals(request.releaseYear, result.releaseYear)
        assertEquals(request.durationMinutes, result.durationMinutes)

        verify(moviePort).save(any<Movie>())
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