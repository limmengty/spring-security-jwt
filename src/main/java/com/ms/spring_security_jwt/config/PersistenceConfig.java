package com.ms.spring_security_jwt.config;

import com.ms.spring_security_jwt.infrastructure.repository.implementation.BaseRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {
        "com.ms.spring_security_jwt.infrastructure.repository",
        "com.ms.spring_security_jwt.modules.auth.repository",
        "com.ms.spring_security_jwt.modules.user.repository"
}, repositoryBaseClass = BaseRepositoryImpl.class)
public class PersistenceConfig {
    @Bean
    AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }

    static class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of("anonymousUser");
        }
    }
}
