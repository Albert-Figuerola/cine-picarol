package com.albert.cinepicarol.movie

import com.albert.cinepicarol.movie.mapper.toResponse
import com.albert.cinepicarol.movie.request.CreateMovieRequest
import com.albert.cinepicarol.movie.response.MovieResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class MovieController (
    private val movieService: MovieService
) {

    @PostMapping("/movies")
    fun createMovie(
        @RequestBody request: CreateMovieRequest
    ) : MovieResponse {
        val movie = movieService.createMovie(request)
        return movie.toResponse()
    }

    @GetMapping("/movies/{id}")
    fun getMovie(
        @PathVariable id: UUID
    ) : MovieResponse {
        val movie = movieService.getMovieById(id)
        return movie.toResponse()
    }

    @GetMapping("/movies")
    fun getMovies() : List<MovieResponse> {
        val movie = movieService.getMovies()
        return movie.toResponse()
    }

}