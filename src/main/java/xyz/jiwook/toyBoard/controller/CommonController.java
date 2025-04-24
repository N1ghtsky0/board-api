package xyz.jiwook.toyBoard.controller;

import jakarta.servlet.http.HttpServletRequest;

public class CommonController {
    protected String getClientIpAddress(HttpServletRequest request) {
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
