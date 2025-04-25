package xyz.jiwook.toyBoard.config.SecurityConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.jiwook.toyBoard.config.exceptionConfig.CustomInvalidJwtException;
import xyz.jiwook.toyBoard.entity.BaseAccountEntity;
import xyz.jiwook.toyBoard.service.TokenService;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessTokenFromHeader(request);
        if (accessToken != null) {
            if (tokenService.ExtractExpirationFromToken(accessToken).before(new Date())) {
                throw new CustomInvalidJwtException("엑세스 토큰이 만료되었습니다.");
            }
            String username = tokenService.ExtractSubjectFromToken(accessToken);
            BaseAccountEntity loginAccount = (BaseAccountEntity) userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginAccount, null, loginAccount.getAuthorities());
            request.setAttribute("authentication", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/auth/token/refresh");
    }
}
