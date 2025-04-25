package xyz.jiwook.toyBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.config.exceptionConfig.CustomInvalidJwtException;
import xyz.jiwook.toyBoard.config.exceptionConfig.CustomLoginFailException;
import xyz.jiwook.toyBoard.dao.MemberRepo;
import xyz.jiwook.toyBoard.dao.RefreshTokenRepo;
import xyz.jiwook.toyBoard.entity.MemberEntity;
import xyz.jiwook.toyBoard.entity.RefreshTokenEntity;
import xyz.jiwook.toyBoard.vo.reponse.TokenVO;
import xyz.jiwook.toyBoard.vo.request.LoginVO;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepo memberRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public String loginProcess(LoginVO loginVO) {
        MemberEntity memberEntity = memberRepo.findByUsername(loginVO.getUsername()).orElseThrow(CustomLoginFailException::new);
        if (!memberEntity.isEnabled()) {
            throw new CustomLoginFailException(memberEntity.getDeletedReason());
        }
        if (!memberEntity.isAccountNonExpired()) {
            throw new CustomLoginFailException("비밀번호 5회 오류");
        }
        if (!passwordEncoder.matches(loginVO.getPassword(), memberEntity.getPassword())) {
            memberEntity.loginFail();
            memberRepo.save(memberEntity);
            throw new CustomLoginFailException();
        }
        memberEntity.loginSuccess();
        memberRepo.save(memberEntity);
        return memberEntity.getUsername();
    }

    public TokenVO reGenerateToken(xyz.jiwook.toyBoard.vo.request.TokenVO oldTokenVO, String ipAddress) {
        String username = tokenService.ExtractSubjectFromToken(oldTokenVO.getAccessToken());
        RefreshTokenEntity savedRefreshToken = refreshTokenRepo.findByTokenAndUsername(oldTokenVO.getRefreshToken(), username)
                .orElseThrow(() -> new CustomInvalidJwtException("유효하지 않은 리프레쉬 토큰입니다."));
        long after3DaysByNow = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000L;
        boolean needToGenerateNewRefreshToken = tokenService.ExtractExpirationFromToken(oldTokenVO.getRefreshToken()).before(new Date(after3DaysByNow));
        String newAccessToken = tokenService.generateAccessToken(username);
        String newRefreshToken = null;
        if (needToGenerateNewRefreshToken) {
            refreshTokenRepo.delete(savedRefreshToken);
            newRefreshToken = tokenService.generateRefreshToken(username, ipAddress);
        }
        return new TokenVO(newAccessToken, newRefreshToken);
    }
}
