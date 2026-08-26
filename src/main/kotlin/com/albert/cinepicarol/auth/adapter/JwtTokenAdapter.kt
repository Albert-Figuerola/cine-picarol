package com.albert.cinepicarol.auth.adapter

import com.albert.cinepicarol.auth.port.TokenPort
import com.albert.cinepicarol.config.JwtProperties
import com.albert.cinepicarol.user.domain.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.Date

@Component
class JwtTokenAdapter(
    private val jwtProperties: JwtProperties
) : TokenPort {

    override fun generateToken(user: User): String {
        val now = Date()
        val expiration = Date(now.time + jwtProperties.expiration)

        val key = Keys.hmacShaKeyFor(
            jwtProperties.secret.toByteArray(StandardCharsets.UTF_8)
        )

        return Jwts.builder()
            .subject(user.id.toString())
            .claim("role", user.role.name)
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key)
            .compact()
    }
}