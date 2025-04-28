package xyz.jiwook.toyBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.config.exceptionConfig.BusinessException;
import xyz.jiwook.toyBoard.dao.AccountRepo;
import xyz.jiwook.toyBoard.dao.RefreshTokenRepo;
import xyz.jiwook.toyBoard.entity.BaseAccountEntity;
import xyz.jiwook.toyBoard.entity.RefreshTokenEntity;
import xyz.jiwook.toyBoard.vo.reponse.TokenVO;
import xyz.jiwook.toyBoard.vo.request.LoginVO;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static xyz.jiwook.toyBoard.config.exceptionConfig.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AccountRepo accountRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 제공된 인증 정보를 검증하고 사용자 계정 상태를 업데이트하여 로그인을 처리합니다.
     * 로그인 실패 시(예: 비활성화된 계정, 잘못된 비밀번호 등) {@link BusinessException}을 발생시킵니다.
     *
     * @param loginVO 사용자가 제공한 아이디와 비밀번호를 포함하는 객체
     * @return 성공적으로 인증된 계정의 사용자명
     * @throws BusinessException 비활성화된 계정, 비밀번호 만료, 잘못된 인증 정보 등으로 인해 로그인 시도가 실패한 경우
     */
    public String loginProcess(LoginVO loginVO) {
        BaseAccountEntity accountEntity = accountRepo.findByUsername(loginVO.getUsername()).orElseThrow(() -> new BusinessException(LOGIN_FAIL_USERNAME_NOT_FOUND));
        if (!accountEntity.isEnabled()) {
            throw new BusinessException(LOGIN_FAIL_IGNORED_USERNAME, accountEntity.getDeletedReason());
        }
        if (!accountEntity.isAccountNonExpired()) {
            throw new BusinessException(LOGIN_FAIL_TOO_MANY_FAIL);
        }
        if (!passwordEncoder.matches(loginVO.getPassword(), accountEntity.getPassword())) {
            accountEntity.loginFail();
            accountRepo.save(accountEntity);
            throw new BusinessException(LOGIN_FAIL_WRONG_PASSWORD);
        }
        accountEntity.loginSuccess();
        accountRepo.save(accountEntity);
        return accountEntity.getUsername();
    }

    public TokenVO reGenerateToken(String oldAccessToken, String oldRefreshToken, String ipAddress) {
        String username = tokenService.ExtractSubjectFromToken(oldAccessToken);
        RefreshTokenEntity savedRefreshToken = refreshTokenRepo.findByTokenAndUsername(oldRefreshToken, username)
                .orElseThrow(() -> new BusinessException(INVALID_REFRESH_TOKEN));
        long after3DaysByNow = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000L;
        boolean needToGenerateNewRefreshToken = tokenService.ExtractExpirationFromToken(oldRefreshToken).before(new Date(after3DaysByNow));
        String newAccessToken = tokenService.generateAccessToken(username);
        String newRefreshToken = null;
        if (needToGenerateNewRefreshToken) {
            refreshTokenRepo.delete(savedRefreshToken);
            newRefreshToken = tokenService.generateRefreshToken(username, ipAddress);
        }
        return new TokenVO(newAccessToken, newRefreshToken);
    }

    public void logoutProcess(String accessToken, String refreshToken) {
        String username = tokenService.ExtractSubjectFromToken(accessToken);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(accessToken, "1", 1000L * 60 * 60, TimeUnit.SECONDS);
        refreshTokenRepo.findByTokenAndUsername(refreshToken, username).ifPresent(refreshTokenRepo::delete);
    }
}