package com.cbkim.apiserver.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// Auditing는 자동설정 되지 않으므로 아래와 같이 수동으로 설정
@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class AuditConfiguration {
    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}