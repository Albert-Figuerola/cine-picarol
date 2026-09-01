package com.albert.cinepicarol.user.port

import com.albert.cinepicarol.user.domain.User
import java.util.UUID

interface UserPort {

    fun findByEmail(email: String): User?

    fun save(user: User): User

    fun findById(id: UUID): User?

}