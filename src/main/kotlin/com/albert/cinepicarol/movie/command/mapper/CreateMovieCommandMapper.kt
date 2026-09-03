package com.albert.cinepicarol.movie.command.mapper

import com.albert.cinepicarol.movie.command.model.CreateMovieCommand
import com.albert.cinepicarol.movie.command.request.CreateMovieRequest

internal fun CreateMovieRequest.toCommand() =
    CreateMovieCommand(
        title = title,
        description = description,
        releaseYear = releaseYear,
        durationMinutes = durationMinutes
    )