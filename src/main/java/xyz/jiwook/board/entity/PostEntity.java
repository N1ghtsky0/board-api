package xyz.jiwook.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.jiwook.board.vo.PostVO;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "post")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    private String thumbnail;

    @ManyToOne
    private MemberEntity author;

    public void updatePost(PostVO postVO) {
        this.title = postVO.getTitle();
        this.content = postVO.getContent();
        this.thumbnail = postVO.getThumbnail();
    }
}
