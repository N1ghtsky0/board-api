package xyz.jiwook.board.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jiwook.board.domain.member.model.LoginVO;
import xyz.jiwook.board.domain.member.model.MemberEntity;
import xyz.jiwook.board.domain.member.repository.MemberCRUDRepo;
import xyz.jiwook.board.global.common.model.CommonDTO;
import xyz.jiwook.board.global.common.util.JWTUtil;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberCRUDRepo memberCRUDRepo;
    private final JWTUtil jwtUtil;

    @Transactional(rollbackFor = Exception.class)
    public void createMember(LoginVO loginVO) {
        memberCRUDRepo.save(new MemberEntity(loginVO, passwordEncoder));
    }

    @Transactional(readOnly = true)
    public CommonDTO loginProcess(LoginVO loginVO) {
        MemberEntity member = memberCRUDRepo.findByUsername(loginVO.getUsername()).orElse(null);
        if (member == null) { return CommonDTO.fail("not found username"); }
        if (!passwordEncoder.matches(loginVO.getPassword(), member.getPassword())) { return CommonDTO.fail("wrong password"); }
        HashMap<String, String> data = new HashMap<>();
        data.put("accessToken", jwtUtil.generateAccessToken(member.getUsername()));
        data.put("refreshToken", jwtUtil.generateRefreshToken());

        return CommonDTO.success(data);
    }
}
