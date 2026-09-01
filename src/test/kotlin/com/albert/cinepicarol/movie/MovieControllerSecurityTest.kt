package com.albert.cinepicarol.movie

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.auth.security.JwtAuthenticationFilter
import com.albert.cinepicarol.config.SecurityConfig
import com.albert.cinepicarol.movie.command.usecase.CreateMovieUseCase
import com.albert.cinepicarol.movie.command.usecase.DeleteMovieUseCase
import com.albert.cinepicarol.movie.command.usecase.UpdateMovieUseCase
import com.albert.cinepicarol.movie.controller.MovieController
import com.albert.cinepicarol.movie.domain.Movie
import com.albert.cinepicarol.movie.domain.MoviesPageDomain
import com.albert.cinepicarol.movie.query.usecase.GetMovieByIdUseCase
import com.albert.cinepicarol.movie.query.usecase.GetMoviesUseCase
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.UUID

@WebMvcTest(MovieController::class)
@Import(
    SecurityConfig::class,
    JwtAuthenticationFilter::class
)
class MovieControllerSecurityTest {

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

    @MockitoBean
    private lateinit var tokenPort: TokenPort

    @Test
    @WithAnonymousUser
    fun `should return 401 when getting movies without authentication`() {
        mockMvc.perform(
            get("/api/v1/movies")
        )
            .andExpect(status().isUnauthorized)

        verifyNoInteractions(getMoviesUseCase)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `should return 200 when USER gets movies`() {
        whenever(getMoviesUseCase.execute(any()))
            .thenReturn(emptyMoviesPage())

        mockMvc.perform(
            get("/api/v1/movies")
        )
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `should return 403 when USER creates movie`() {
        mockMvc.perform(
            post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateMovieRequest())
        )
            .andExpect(status().isForbidden)

        verifyNoInteractions(createMovieUseCase)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should return 201 when ADMIN creates movie`() {
        whenever(createMovieUseCase.execute(any()))
            .thenReturn(createMovie())

        mockMvc.perform(
            post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validCreateMovieRequest())
        )
            .andExpect(status().isCreated)
    }

    private fun createMovie() = Movie(
        id = UUID.randomUUID(),
        title = "Titanic",
        description = "Titanic description",
        releaseYear = 1997,
        durationMinutes = 194,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    private fun emptyMoviesPage() = MoviesPageDomain(
        movies = emptyList(),
        currentPage = 0,
        pageSize = 10,
        totalPages = 0,
        totalElements = 0,
        hasPrevious = false,
        hasNext = false
    )

    private fun validCreateMovieRequest() = """
        {
            "title": "Titanic",
            "description": "Titanic description",
            "releaseYear": 1997,
            "durationMinutes": 194
        }
    """.trimIndent()
}
