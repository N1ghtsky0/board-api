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
import xyz.jiwook.toyBoard.config.exceptionConfig.GlobalExceptionHandler;
import xyz.jiwook.toyBoard.entity.BaseAccountEntity;
import xyz.jiwook.toyBoard.service.TokenService;
import xyz.jiwook.toyBoard.util.HttpContextUtils;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;
    private final HttpContextUtils httpContextUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String accessToken = httpContextUtils.extractAccessToken(request);
        if (accessToken != null) {
            if (tokenService.ExtractExpirationFromToken(accessToken).before(new Date())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("엑세스 토큰이 만료되었습니다.");
                return;
            }
            String username = tokenService.ExtractSubjectFromToken(accessToken);
            BaseAccountEntity loginAccount = (BaseAccountEntity) userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginAccount, null, loginAccount.getAuthorities());
            authentication.setDetails(loginAccount);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/auth/token/refresh");
    }
}
