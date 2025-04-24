package xyz.jiwook.toyBoard.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "account")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type")
public abstract class BaseAccountEntity extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private byte loginFailCount;

    protected BaseAccountEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.loginFailCount = 0;
    }

    public void loginFail() {
        this.loginFailCount++;
    }

    public void loginSuccess() {
        this.loginFailCount = 0;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return !super.isDeleted();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.loginFailCount < 5;
    }
}
