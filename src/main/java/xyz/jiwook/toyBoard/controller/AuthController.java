package xyz.jiwook.toyBoard.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jiwook.toyBoard.service.MemberService;
import xyz.jiwook.toyBoard.util.TokenUtil;
import xyz.jiwook.toyBoard.vo.reponse.TokenVO;
import xyz.jiwook.toyBoard.vo.request.LoginVO;
import xyz.jiwook.toyBoard.vo.request.RegisterVO;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController extends CommonController {
    private final MemberService memberService;
    private final TokenUtil tokenUtil;

    @PostMapping("/member/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterVO registerVO) {
        memberService.register(registerVO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/member/login")
    public ResponseEntity<TokenVO> login(@RequestBody @Valid LoginVO loginVO, HttpServletRequest request) {
        String loginUsername = memberService.loginProcess(loginVO);
        String accessToken = tokenUtil.generateAccessToken(loginUsername);
        String refreshToken = tokenUtil.generateRefreshToken(loginUsername, getClientIpAddress(request));
        return ResponseEntity.ok(new TokenVO(accessToken, refreshToken));
    }
}
