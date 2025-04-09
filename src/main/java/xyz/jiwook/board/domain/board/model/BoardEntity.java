package xyz.jiwook.board.domain.board.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.jiwook.board.domain.member.model.MemberEntity;
import xyz.jiwook.board.global.common.model.BaseTimeEntity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board")
public class BoardEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;

    @Builder
    public BoardEntity(String title, String content, MemberEntity member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
