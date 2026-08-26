package com.albert.cinepicarol.auth.port

import com.albert.cinepicarol.user.domain.User

interface TokenPort {

    fun generateToken(user: User): String

}