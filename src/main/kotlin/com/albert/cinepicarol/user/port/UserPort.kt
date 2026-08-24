package com.albert.cinepicarol.user.port

import com.albert.cinepicarol.user.domain.User

interface UserPort {

    fun findByEmail(email: String): User?

    fun save(user: User): User

}