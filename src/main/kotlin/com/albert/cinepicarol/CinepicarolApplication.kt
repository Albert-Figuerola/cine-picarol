package com.albert.cinepicarol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class CinepicarolApplication

fun main(args: Array<String>) {
    runApplication<CinepicarolApplication>(*args)
}