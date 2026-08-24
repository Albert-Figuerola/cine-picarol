package com.albert.cinepicarol.user.port

interface PasswordEncoderPort {

    fun encode(rawPassword: String): String

}