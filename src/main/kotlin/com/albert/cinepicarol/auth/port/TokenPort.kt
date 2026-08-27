package com.albert.cinepicarol.auth.port

import com.albert.cinepicarol.user.domain.User
import com.albert.cinepicarol.user.domain.UserRole
import java.util.UUID

interface TokenPort {

    fun generateToken(user: User): String

    fun getUserId(token: String): UUID

    fun getRole(token: String): UserRole

    fun isValid(token: String): Boolean

}