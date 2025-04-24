package xyz.jiwook.toyBoard.vo.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.jiwook.toyBoard.util.ValidPassword;
import xyz.jiwook.toyBoard.util.ValidUsername;

@Getter
@Setter
public class RegisterVO {
    @ValidUsername
    private String username;

    @ValidPassword
    private String password;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
