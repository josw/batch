package com.josw.batch.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.josw.repository"])
@EntityScan(basePackages = ["com.josw.entity"])
class AppConfig {
}