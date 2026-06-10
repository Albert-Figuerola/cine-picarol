package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.controller.MovieController
import com.albert.cinepicarol.movie.entity.MovieEntity
import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import com.albert.cinepicarol.movie.command.usecase.CreateMovieUseCase
import com.albert.cinepicarol.movie.command.usecase.DeleteMovieUseCase
import com.albert.cinepicarol.movie.query.usecase.GetMovieByIdUseCase
import com.albert.cinepicarol.movie.query.usecase.GetMoviesUseCase
import com.albert.cinepicarol.movie.command.usecase.UpdateMovieUseCase
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.UUID

@WebMvcTest(MovieController::class)
@AutoConfigureMockMvc(addFilters = false)
class MovieControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var createMovieUseCase: CreateMovieUseCase

    @MockitoBean
    private lateinit var getMovieByIdUseCase: GetMovieByIdUseCase

    @MockitoBean
    private lateinit var getMoviesUseCase: GetMoviesUseCase

    @MockitoBean
    private lateinit var updateMovieUseCase: UpdateMovieUseCase

    @MockitoBean
    private lateinit var deleteMovieUseCase: DeleteMovieUseCase

    @Test
    fun `should return 200 when movie exists`() {
        val movie = createMovie()

        whenever(getMovieByIdUseCase.execute(movie.id))
            .thenReturn(movie)

        mockMvc.perform(
            get("/movies/{id}", movie.id)
        )

            .andExpect(status().isOk)

            .andExpect(jsonPath("$.title").value("Titanic"))
    }

    @Test
    fun `should return 400 when title is empty`() {
        val request = """
        {
            "title": "",
            "description": "Description",
            "releaseYear": 1997,
            "durationMinutes": 194
        }
    """.trimIndent()

        mockMvc.perform(
            post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(
                jsonPath("$.message")
                    .value("Movie title cannot be empty")
            )

        verifyNoInteractions(createMovieUseCase)
    }

    @Test
    fun `should return 400 when title contains only spaces`() {
        val request = """
        {
            "title": "     ",
            "description": "Description",
            "releaseYear": 1997,
            "durationMinutes": 194
        }
    """.trimIndent()

        mockMvc.perform(
            post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(
                jsonPath("$.message")
                    .value("Movie title cannot be empty")
            )

        verifyNoInteractions(createMovieUseCase)
    }

    @Test
    fun `should return 400 when description is empty`() {
        val request = """
        {
            "title": "Titanic",
            "description": "",
            "releaseYear": 1997,
            "durationMinutes": 194
        }
    """.trimIndent()

        mockMvc.perform(
            post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(
                jsonPath("$.message")
                    .value("Movie description cannot be empty")
            )

        verifyNoInteractions(createMovieUseCase)
    }

    @Test
    fun `should return 400 when duration is not positive`() {
        val request = """
        {
            "title": "Titanic",
            "description": "Description",
            "releaseYear": 1997,
            "durationMinutes": 0
        }
    """.trimIndent()

        mockMvc.perform(
            post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
            .andExpect(
                jsonPath("$.message")
                    .value("Movie duration minutes must be greater than zero")
            )

        verifyNoInteractions(createMovieUseCase)
    }

    @Test
    fun `should return 404 when movie does not exist`() {
        val movieId = UUID.randomUUID()

        whenever(getMovieByIdUseCase.execute(movieId))
            .thenThrow(MovieNotFoundException(movieId))

        mockMvc.perform(
            get("/movies/{id}", movieId)
        )

            .andExpect(status().isNotFound)

            .andExpect(
                jsonPath("$.message")
                    .value("Movie with id $movieId not found")
            )
    }

    @Test
    fun `should return 204 when movie is deleted`() {
        val movie = createMovie()

        mockMvc.perform(
            delete("/movies/{id}", movie.id)
        )
            .andExpect(status().isNoContent)
    }

    @Test
    fun `should return 404 when movie to delete does not exist`() {
        val movieId = UUID.randomUUID()

        doThrow(MovieNotFoundException(movieId))
            .whenever(deleteMovieUseCase)
            .execute(movieId)

        mockMvc.perform(
            delete("/movies/{id}", movieId)
        )
            .andExpect(status().isNotFound)
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