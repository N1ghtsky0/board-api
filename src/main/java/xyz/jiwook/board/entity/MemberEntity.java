package xyz.jiwook.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.jiwook.board.vo.UserInfoVO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "member")
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String introduction;

    private String profileImage;

    public void updateProfile(UserInfoVO userInfoVO) {
        this.nickname = userInfoVO.getKickName();
        this.introduction = userInfoVO.getIntroduce();
        this.profileImage = userInfoVO.getThumbnail();
    }
}
