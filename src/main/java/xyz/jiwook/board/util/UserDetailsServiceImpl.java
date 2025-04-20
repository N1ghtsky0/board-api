package xyz.jiwook.board.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.jiwook.board.dao.MemberRepo;
import xyz.jiwook.board.entity.MemberEntity;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepo memberRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserDetailsImpl(memberEntity);
    }
}
