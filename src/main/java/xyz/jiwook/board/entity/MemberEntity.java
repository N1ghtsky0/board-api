package xyz.jiwook.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.jiwook.board.vo.UserInfoVO;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("member")
public class MemberEntity extends BaseAccountEntity {
    @Column(nullable = false, unique = true)
    private String nickname;

    private String introduction;

    private String profileImage;

    public MemberEntity(String username, String password) {
        super(username, password);
        this.nickname = username;
    }

    public void updateProfile(UserInfoVO userInfoVO) {
        this.nickname = userInfoVO.getKickName();
        this.introduction = userInfoVO.getIntroduce();
        this.profileImage = userInfoVO.getThumbnail();
    }
}
