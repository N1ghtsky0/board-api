package xyz.jiwook.toyBoard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.jiwook.toyBoard.dao.MemberRepo;
import xyz.jiwook.toyBoard.entity.MemberEntity;
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
}
