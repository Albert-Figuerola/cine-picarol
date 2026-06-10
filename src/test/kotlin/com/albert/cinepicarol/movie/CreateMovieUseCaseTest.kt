package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.command.request.CreateMovieRequest
import com.albert.cinepicarol.movie.command.usecase.CreateMovieUseCase
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class CreateMovieUseCaseTest {

    private val movieRepository = mock<MovieRepository>()
    private val createMovieUseCase = CreateMovieUseCase(movieRepository)

    @Test
    fun `should create movie`() {
        val request = createMovieRequest()

        whenever(movieRepository.save(any<MovieEntity>()))
            .thenAnswer { it.arguments[0] as MovieEntity }

        val result = createMovieUseCase.execute(request)

        assertEquals(request.title, result.title)
        assertEquals(request.description, result.description)
        assertEquals(request.releaseYear, result.releaseYear)
        assertEquals(request.durationMinutes, result.durationMinutes)

        verify(movieRepository).save(any<MovieEntity>())
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