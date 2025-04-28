package xyz.jiwook.toyBoard.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jiwook.toyBoard.service.AuthService;
import xyz.jiwook.toyBoard.service.TokenService;
import xyz.jiwook.toyBoard.util.HttpContextUtils;
import xyz.jiwook.toyBoard.vo.reponse.TokenVO;
import xyz.jiwook.toyBoard.vo.request.LoginVO;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;
    private final HttpContextUtils httpContextUtils;

    @PostMapping("/login")
    public ResponseEntity<TokenVO> login(@RequestBody @Valid LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) {
        String loginUsername = authService.loginProcess(loginVO);
        String accessToken = tokenService.generateAccessToken(loginUsername);
        String refreshToken = tokenService.generateRefreshToken(loginUsername, httpContextUtils.getClientIpAddress(request));
        this.setAuthorization(response, accessToken);
        this.setRefreshTokenCookie(response, refreshToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenVO> reGenerateToken(HttpServletRequest request, HttpServletResponse response) {
        String oldAccessToken = httpContextUtils.extractAccessToken(request);
        String oldRefreshToken = httpContextUtils.extractRefreshToken(request);
        TokenVO tokenVO = authService.reGenerateToken(oldAccessToken, oldRefreshToken, httpContextUtils.getClientIpAddress(request));
        setAuthorization(response, tokenVO.accessToken());
        setRefreshTokenCookie(response, tokenVO.refreshToken());
        return ResponseEntity.ok().build();
    }

    private void setAuthorization(HttpServletResponse response, String accessToken) {
        if (accessToken == null) { return; }
        response.setHeader("Authorization", "Bearer " + accessToken);
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        if (refreshToken == null) { return; }
        Cookie refreshTokenCookie = new Cookie("refresh-token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }
}
