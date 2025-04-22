package xyz.jiwook.board.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.jiwook.board.entity.AdminEntity;
import xyz.jiwook.board.entity.BaseAccountEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record UserDetailsImpl(BaseAccountEntity accountEntity) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (accountEntity instanceof AdminEntity) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.accountEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return this.accountEntity.getUsername();
    }
}
