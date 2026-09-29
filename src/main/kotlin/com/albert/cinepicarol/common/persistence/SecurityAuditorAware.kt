package com.albert.cinepicarol.common.persistence

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.Optional
import java.util.UUID

@Component
class SecurityAuditorAware : AuditorAware<UUID> {

    override fun getCurrentAuditor(): Optional<UUID> {
        val authentication = SecurityContextHolder
            .getContext()
            .authentication

        val principal = authentication?.principal

        return if (principal is UUID) {
            Optional.of(principal)
        } else {
            Optional.empty()
        }
    }

}