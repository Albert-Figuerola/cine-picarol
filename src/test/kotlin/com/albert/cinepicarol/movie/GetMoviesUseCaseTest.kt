package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.repository.MovieRepository
import com.albert.cinepicarol.movie.query.usecase.GetMoviesUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.UUID

class GetMoviesUseCaseTest {

    private val movieRepository = mock<MovieRepository>()
    private val getMoviesUseCase = GetMoviesUseCase(movieRepository)

    @Test
    fun `should return paginated movies`() {
        val pageable = PageRequest.of(0, 10)

        val movies = listOf(
            createMovie(title = "Titanic"),
            createMovie(title = "Interstellar")
        )

        val page = PageImpl(movies, pageable, movies.size.toLong())

        whenever(movieRepository.findAll(pageable))
            .thenReturn(page)

        val result = getMoviesUseCase.execute(pageable)

        assertEquals(2, result.content.size)
        assertEquals("Titanic", result.content[0].title)
        assertEquals("Interstellar", result.content[1].title)
        assertEquals(2, result.totalElements)

        verify(movieRepository).findAll(pageable)
    }

    private fun createMovie(
        title: String = "Title test"
    ): MovieEntity {
        return MovieEntity(
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