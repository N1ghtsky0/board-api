package xyz.jiwook.toyBoard.entity;

import jakarta.persistence.*;
import lombok.*;
import xyz.jiwook.toyBoard.vo.request.EditPostVO;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "post")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private BaseAccountEntity author;

    @Builder
    public PostEntity(String title, String content, BaseAccountEntity author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(EditPostVO editPostVO) {
        this.title = editPostVO.getTitle();
        this.content = editPostVO.getContent();
    }
}
