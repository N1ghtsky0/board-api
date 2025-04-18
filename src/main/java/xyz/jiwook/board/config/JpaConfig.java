package xyz.jiwook.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import xyz.jiwook.board.util.SecurityAuditorAware;

@Configuration
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new SecurityAuditorAware();
    }
}
