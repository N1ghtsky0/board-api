package xyz.jiwook.board.domain.member.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.jiwook.board.global.common.model.BaseTimeEntity;
import xyz.jiwook.board.global.common.model.CommonDTO;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member")
public class MemberEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public MemberEntity(LoginVO loginVO, PasswordEncoder passwordEncoder) {
        this.uuid = UUID.randomUUID().toString();
        this.username = loginVO.getUsername();
        this.password = passwordEncoder.encode(loginVO.getPassword());
    }

    public CommonDTO changePassword(String currentPassword, String newPassword, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(currentPassword, password)) {
            this.password = passwordEncoder.encode(newPassword);
            return CommonDTO.success();
        }
        return CommonDTO.fail("wrong password");
    }
}
