package com.albert.cinepicarol.common.persistence

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing(auditorAwareRef = "securityAuditorAware")
class JpaAuditingConfig