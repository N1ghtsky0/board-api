package xyz.jiwook.toyBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.config.exceptionConfig.CustomLoginFailException;
import xyz.jiwook.toyBoard.dao.MemberRepo;
import xyz.jiwook.toyBoard.entity.MemberEntity;
import xyz.jiwook.toyBoard.vo.request.LoginVO;
import xyz.jiwook.toyBoard.vo.request.RegisterVO;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepo memberRepo;

    public void register(RegisterVO registerVO) {
        registerVO.encodePassword(passwordEncoder);
        memberRepo.save(new MemberEntity(registerVO));
    }

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
}
