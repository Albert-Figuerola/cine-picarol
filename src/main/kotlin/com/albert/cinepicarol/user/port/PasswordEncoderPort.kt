package com.albert.cinepicarol.user.port

interface PasswordEncoderPort {

    fun encode(rawPassword: String): String

    fun matches(
        rawPassword: String,
        encodedPassword: String
    ): Boolean

}