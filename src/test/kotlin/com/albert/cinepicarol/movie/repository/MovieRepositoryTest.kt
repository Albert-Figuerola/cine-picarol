package com.albert.cinepicarol.movie.repository

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.movie.entity.MovieEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest
class MovieRepositoryTest {

    @Autowired
    private lateinit var movieRepository: MovieRepository

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun shouldSaveMovie() {
        val movie = MovieEntity(
            id = UUID.randomUUID(),
            title = "Test Movie",
            description = "Test Description",
            releaseYear = 2023,
            durationMinutes = 120
        )

        val savedMovie = movieRepository.saveAndFlush(movie)

        val foundMovie = movieRepository.findByIdOrNull(savedMovie.id)

        assertNotNull(foundMovie)
        assertEquals(movie.id, foundMovie.id)
        assertEquals(movie.title, foundMovie.title)
        assertEquals(movie.description, foundMovie.description)
        assertEquals(movie.releaseYear, foundMovie.releaseYear)
    }

    @Test
    fun `should audit authenticated user`() {
        val userId = UUID.randomUUID()

        val authentication = UsernamePasswordAuthenticationToken(
            userId,
            null,
            emptyList()
        )

        SecurityContextHolder.getContext().authentication = authentication

        val movie = MovieEntity(
            id = UUID.randomUUID(),
            title = "Test Movie",
            description = "Test Description",
            releaseYear = 2023,
            durationMinutes = 120
        )

        val savedMovie = movieRepository.saveAndFlush(movie)

        assertEquals(userId, savedMovie.createdById)
        assertEquals(userId, savedMovie.updatedById)
    }

    @Test
    fun `should audit user when movie is updated`() {
        val createdByUserId = UUID.randomUUID()

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(
                createdByUserId,
                null,
                emptyList()
            )

        val movie = MovieEntity(
            id = UUID.randomUUID(),
            title = "Test Movie",
            description = "Test Description",
            releaseYear = 2023,
            durationMinutes = 120
        )

        val savedMovie = movieRepository.saveAndFlush(movie)

        val updatedByUserId = UUID.randomUUID()

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(
                updatedByUserId,
                null,
                emptyList()
            )

        savedMovie.title = "Updated title"
        savedMovie.description = "Updated description"

        val updatedMovie = movieRepository.saveAndFlush(savedMovie)

        assertEquals(createdByUserId, updatedMovie.createdById)
        assertEquals(updatedByUserId, updatedMovie.updatedById)
    }

}