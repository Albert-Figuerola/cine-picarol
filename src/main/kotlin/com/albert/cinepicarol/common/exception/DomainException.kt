package com.albert.cinepicarol.common.exception

abstract class DomainException(
    val code: String,
    override val message: String
) : RuntimeException(message)