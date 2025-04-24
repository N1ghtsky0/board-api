package xyz.jiwook.toyBoard.config.jpaConfig;

import org.springframework.data.domain.AuditorAware;
import xyz.jiwook.toyBoard.config.SecurityConfig.SecurityUtil;

import java.util.Optional;

public class JpaAuditorAware implements AuditorAware<String> {
    private final SecurityUtil securityUtil;
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(securityUtil.getCurrentUsername());
    }

    public JpaAuditorAware(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }
}
