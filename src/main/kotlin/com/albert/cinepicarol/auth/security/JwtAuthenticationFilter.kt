package com.albert.cinepicarol.auth.security

import com.albert.cinepicarol.auth.port.TokenPort
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val tokenPort: TokenPort
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (
            authorizationHeader == null ||
            !authorizationHeader.startsWith("Bearer ")
        ) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authorizationHeader.removePrefix("Bearer ")

        if (!tokenPort.isValid(token)) {
            filterChain.doFilter(request, response)
            return
        }

        val userId = tokenPort.getUserId(token)
        val role = tokenPort.getRole(token)

        val authorities = listOf(
            SimpleGrantedAuthority("ROLE_${role.name}")
        )

        val authentication =
            UsernamePasswordAuthenticationToken(
                userId,
                null,
                authorities
            )

        SecurityContextHolder
            .getContext()
            .authentication = authentication

        filterChain.doFilter(request, response)
    }

}