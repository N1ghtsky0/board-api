package xyz.jiwook.toyBoard.config.jpaConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import xyz.jiwook.toyBoard.config.SecurityConfig.SecurityUtil;

@RequiredArgsConstructor
@Configuration
@EnableJpaAuditing
public class JpaConfig {
    private final SecurityUtil securityUtil;
    @Bean
    public AuditorAware<String> auditorAware() {
        return new JpaAuditorAware(securityUtil);
    }
}
