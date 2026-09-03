package com.albert.cinepicarol.movie.command.mapper

import com.albert.cinepicarol.movie.command.model.UpdateMovieCommand
import com.albert.cinepicarol.movie.command.request.UpdateMovieRequest

internal fun UpdateMovieRequest.toCommand() =
    UpdateMovieCommand(
        title = title,
        description = description,
        releaseYear = releaseYear,
        durationMinutes = durationMinutes
    )