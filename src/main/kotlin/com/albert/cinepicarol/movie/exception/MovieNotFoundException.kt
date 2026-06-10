package com.albert.cinepicarol.movie.exception

import com.albert.cinepicarol.common.exception.DomainException
import java.util.UUID

class MovieNotFoundException(
    id: UUID
) : DomainException (
    code = "MOVIE_NOT_FOUND",
    message = "Movie with id $id not found"
)