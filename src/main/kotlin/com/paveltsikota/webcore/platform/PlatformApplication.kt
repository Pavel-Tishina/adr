package com.paveltsikota.webcore.platform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.paveltsikota.webcore"])
@EntityScan("com.paveltsikota.webcore.db.entity")
@EnableJpaRepositories("com.paveltsikota.webcore.db.service.impl.dao")
class PlatformApplication

fun main(args: Array<String>) {
	runApplication<PlatformApplication>(*args)
}
