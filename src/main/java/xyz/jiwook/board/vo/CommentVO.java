package xyz.jiwook.board.vo;

import lombok.Data;

@Data
public class CommentVO {
    private Long id;
    private String author;
    private String content;
    private String regDt;
    private String likeCount;
    private String dislikeCount;
}
