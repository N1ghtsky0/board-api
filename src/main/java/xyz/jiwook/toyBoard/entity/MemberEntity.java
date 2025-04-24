package xyz.jiwook.toyBoard.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import xyz.jiwook.toyBoard.util.AccountType;
import xyz.jiwook.toyBoard.vo.request.RegisterVO;

import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("member")
public class MemberEntity extends BaseAccountEntity {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(AccountType.MEMBER.name()));
    }

    public MemberEntity(RegisterVO registerVO) {
        super(registerVO.getUsername(), registerVO.getPassword());
    }
}
