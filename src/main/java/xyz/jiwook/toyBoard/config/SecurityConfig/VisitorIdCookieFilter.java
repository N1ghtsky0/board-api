package xyz.jiwook.toyBoard.config.SecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.jiwook.toyBoard.util.HttpContextUtils;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class VisitorIdCookieFilter extends OncePerRequestFilter {
    private final HttpContextUtils httpContextUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String visitorId = httpContextUtils.extractVisitorId(request);
        if (visitorId == null) {
            Cookie visitorIdCookie = new Cookie("vid", UUID.randomUUID().toString());
            visitorIdCookie.setHttpOnly(true);
            visitorIdCookie.setPath("/");
            visitorIdCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(visitorIdCookie);
        }
        filterChain.doFilter(request, response);
    }
}
