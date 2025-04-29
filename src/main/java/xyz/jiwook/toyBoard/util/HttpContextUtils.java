package xyz.jiwook.toyBoard.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import static xyz.jiwook.toyBoard.util.Constants.*;

@Component
public class HttpContextUtils {
    public String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String extractRefreshToken(HttpServletRequest request) {
        return this.getCookieValue(request, REFRESH_TOKEN_COOKIE_NAME);
    }

    public String extractVisitorId(HttpServletRequest request) {
        return this.getCookieValue(request, VISITOR_COOKIE_NAME);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) { return null; }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
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
