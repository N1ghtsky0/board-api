package xyz.jiwook.toyBoard.config.SecurityConfig;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtil {
    public String getCurrentUsername() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null || currentUser instanceof AnonymousAuthenticationToken) {
            return "anonymous";
        }
        return currentUser.getName();
    }
}
