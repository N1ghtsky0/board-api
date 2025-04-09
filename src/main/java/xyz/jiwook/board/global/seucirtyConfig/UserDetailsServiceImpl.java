package xyz.jiwook.board.global.seucirtyConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.jiwook.board.domain.member.model.MemberEntity;
import xyz.jiwook.board.domain.member.repository.MemberCRUDRepo;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberCRUDRepo memberCRUDRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity member = memberCRUDRepo.findByUuid(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(member.getUsername(), member.getPassword(), Collections.singleton(new SimpleGrantedAuthority("USER")));
    }
}
