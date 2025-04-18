package xyz.jiwook.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jiwook.board.dao.MemberRepo;
import xyz.jiwook.board.entity.MemberEntity;
import xyz.jiwook.board.util.TokenUtil;
import xyz.jiwook.board.vo.TokenVO;
import xyz.jiwook.board.vo.UserInfoVO;
import xyz.jiwook.board.vo.UsernamePasswordVO;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;
    private final MemberRepo memberRepo;

    @Transactional(rollbackFor = Exception.class)
    public void createMember(UsernamePasswordVO usernamePasswordVO) {
        memberRepo.save(MemberEntity.builder()
                .username(usernamePasswordVO.getUsername())
                .password(passwordEncoder.encode(usernamePasswordVO.getPassword()))
                .nickname(usernamePasswordVO.getUsername())
                .build());
    }

    @Transactional(readOnly = true)
    public TokenVO loginProcess(UsernamePasswordVO usernamePasswordVO) {
        MemberEntity memberEntity = memberRepo.findByUsername(usernamePasswordVO.getUsername()).orElse(new MemberEntity());
        if (memberEntity.getUsername() == null ||
                !passwordEncoder.matches(usernamePasswordVO.getPassword(), memberEntity.getPassword())) {
            return new TokenVO();
        }
        return new TokenVO(tokenUtil.generateAccessToken(memberEntity.getUsername()), tokenUtil.generateRefreshToken());
    }

    @Transactional(readOnly = true)
    public UserInfoVO findUserInfoByUsername(String username) {
        return memberRepo.selectUserInfoByUsername(username);
    }
}
