package com.albert.cinepicarol.movie.controller

import com.albert.cinepicarol.common.response.ApiResponse
import com.albert.cinepicarol.movie.command.mapper.toCommand
import com.albert.cinepicarol.movie.mapper.toResponse
import com.albert.cinepicarol.movie.command.request.CreateMovieRequest
import com.albert.cinepicarol.movie.command.request.UpdateMovieRequest
import com.albert.cinepicarol.movie.query.response.MovieResponse
import com.albert.cinepicarol.movie.query.response.MoviesPageResponse
import com.albert.cinepicarol.movie.command.usecase.CreateMovieUseCase
import com.albert.cinepicarol.movie.command.usecase.DeleteMovieUseCase
import com.albert.cinepicarol.movie.query.usecase.GetMovieByIdUseCase
import com.albert.cinepicarol.movie.query.usecase.GetMoviesUseCase
import com.albert.cinepicarol.movie.command.usecase.UpdateMovieUseCase
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/movies")
class MovieController (
    private val createMovieUseCase: CreateMovieUseCase,
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val deleteMovieUseCase: DeleteMovieUseCase
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createMovie(
        @Valid
        @RequestBody request: CreateMovieRequest
    ) : ApiResponse<MovieResponse> {
        val movie = createMovieUseCase.execute(request.toCommand())
        return ApiResponse(
            data = movie.toResponse()
        )
    }

    @GetMapping("/{id}")
    fun getMovie(
        @PathVariable id: UUID
    ) : ApiResponse<MovieResponse> {
        val movie = getMovieByIdUseCase.execute(id)
        return ApiResponse(
            data = movie.toResponse()
        )
    }

    @GetMapping
    fun getMovies(
        @PageableDefault(size = 10, sort = ["title"], direction = Sort.Direction.DESC) pageable: Pageable
    ) : MoviesPageResponse {
        val movies = getMoviesUseCase.execute(pageable)
        return movies.toResponse()
    }

    @PatchMapping("/{id}")
    fun updateMovie(
        @PathVariable id: UUID,
        @RequestBody request: UpdateMovieRequest,
    ) : ApiResponse<MovieResponse> {
        val movies = updateMovieUseCase.execute(id, request.toCommand())
        return ApiResponse(
            data = movies.toResponse()
        )
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMovie(
        @PathVariable id: UUID
    ) {
        return deleteMovieUseCase.execute(id)
    }

}