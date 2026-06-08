package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.request.CreateMovieRequest
import com.albert.cinepicarol.movie.usecase.CreateMovieUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.never
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

    @Test
    fun `should not save movie when title is empty`() {
        val request = createMovieRequest(title = "")

        assertThrows<IllegalArgumentException> {
            createMovieUseCase.execute(request)
        }

        verify(movieRepository, never())
            .save(any<MovieEntity>())
    }

    @Test
    fun `should throw exception when title is empty`() {
        val request = createMovieRequest(title = "")

        assertThrows<IllegalArgumentException> {
            createMovieUseCase.execute(request)
        }
    }

    @Test
    fun `should throw exception when title contains only spaces`() {
        val request = createMovieRequest(title = "     ")

        assertThrows<IllegalArgumentException> {
            createMovieUseCase.execute(request)
        }
    }

    @Test
    fun `should throw exception when description is empty`() {
        val request = createMovieRequest(description = "")

        assertThrows<IllegalArgumentException> {
            createMovieUseCase.execute(request)
        }
    }

    @Test
    fun `should throw exception when description contains only spaces`() {
        val request = createMovieRequest(description = "     ")

        assertThrows<IllegalArgumentException> {
            createMovieUseCase.execute(request)
        }
    }

    @Test
    fun `should throw exception when duration is zero`() {
        val request = createMovieRequest(durationMinutes = 0)

        assertThrows<IllegalArgumentException> {
            createMovieUseCase.execute(request)
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