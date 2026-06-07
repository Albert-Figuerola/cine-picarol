package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.exception.MovieNotFoundException
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
    private lateinit var movieService: MovieService

    @Test
    fun `should return 200 when movie exists`() {
        val movie = MovieEntity(
            id = UUID.randomUUID(),
            title = "Titanic",
            description = "Description",
            releaseYear = 1997,
            durationMinutes = 194,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        whenever(movieService.getMovieById(movie.id))
            .thenReturn(movie)

        mockMvc.perform(
            get("/movies/{id}", movie.id)
        )

            .andExpect(status().isOk)

            .andExpect(jsonPath("$.title").value("Titanic"))
    }

    @Test
    fun `should return 404 when movie does not exist`() {
        val movieId = UUID.randomUUID()

        whenever(movieService.getMovieById(movieId))
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

}