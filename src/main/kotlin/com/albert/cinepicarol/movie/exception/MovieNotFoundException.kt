package com.albert.cinepicarol.movie.exception

import java.util.UUID

class MovieNotFoundException(
    id: UUID
) : RuntimeException(
    "Movie with id $id not found"
)