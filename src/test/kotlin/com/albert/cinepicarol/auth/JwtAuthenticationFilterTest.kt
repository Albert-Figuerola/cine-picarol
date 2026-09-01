package com.albert.cinepicarol.auth

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.auth.security.JwtAuthenticationFilter
import com.albert.cinepicarol.config.SecurityConfig
import com.albert.cinepicarol.movie.command.usecase.CreateMovieUseCase
import com.albert.cinepicarol.movie.command.usecase.DeleteMovieUseCase
import com.albert.cinepicarol.movie.command.usecase.UpdateMovieUseCase
import com.albert.cinepicarol.movie.controller.MovieController
import com.albert.cinepicarol.movie.domain.MoviesPageDomain
import com.albert.cinepicarol.movie.query.usecase.GetMovieByIdUseCase
import com.albert.cinepicarol.movie.query.usecase.GetMoviesUseCase
import com.albert.cinepicarol.user.domain.UserRole
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(MovieController::class)
@Import(
    SecurityConfig::class,
    JwtAuthenticationFilter::class
)
class JwtAuthenticationFilterTest {

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
    fun `should authenticate request when bearer token is valid`() {
        whenever(tokenPort.isValid("valid-token"))
            .thenReturn(true)

        whenever(tokenPort.getUserId("valid-token"))
            .thenReturn(UUID.randomUUID())

        whenever(tokenPort.getRole("valid-token"))
            .thenReturn(UserRole.USER)

        whenever(getMoviesUseCase.execute(any()))
            .thenReturn(emptyMoviesPage())

        mockMvc.perform(
            get("/movies")
                .header(
                    "Authorization",
                    "Bearer valid-token"
                )
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 401 when bearer token is invalid`() {
        whenever(tokenPort.isValid("invalid-token"))
            .thenReturn(false)

        mockMvc.perform(
            get("/movies")
                .header(
                    "Authorization",
                    "Bearer invalid-token"
                )
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return 401 when authorization header does not use bearer`() {
        mockMvc.perform(
            get("/movies")
                .header(
                    "Authorization",
                    "Basic something"
                )
        )
            .andExpect(status().isUnauthorized)
    }

    private fun emptyMoviesPage() = MoviesPageDomain(
        movies = emptyList(),
        currentPage = 0,
        pageSize = 10,
        totalPages = 0,
        totalElements = 0,
        hasPrevious = false,
        hasNext = false
    )

}