package xyz.jiwook.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jiwook.board.dao.MemberRepo;
import xyz.jiwook.board.entity.MemberEntity;
import xyz.jiwook.board.vo.UsernamePasswordVO;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepo memberRepo;

    @Transactional(rollbackFor = Exception.class)
    public void createMember(UsernamePasswordVO usernamePasswordVO) {
        memberRepo.save(MemberEntity.builder()
                .username(usernamePasswordVO.getUsername())
                .password(passwordEncoder.encode(usernamePasswordVO.getPassword()))
                .nickname(usernamePasswordVO.getUsername())
                .build());
    }
}
