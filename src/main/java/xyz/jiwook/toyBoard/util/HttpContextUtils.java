package xyz.jiwook.toyBoard.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpContextUtils {
    public String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) { return null; }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh-token")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "X-Real-IP",
                "CF-Connecting-IP",
                "True-Client-IP",
                "X-Client-IP",
                "Fastly-Client-IP",
                "X-Cluster-Client-IP",
                "Forwarded"
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For일 경우 여러 개의 IP가 들어올 수 있음
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}
