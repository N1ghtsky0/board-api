package xyz.jiwook.board.vo;

import lombok.Data;

@Data
public class PostVO {
    private Long id;
    private String title;
    private String content;
    private String updDt;
    private String author;
    private String thumbnail;
    private int likeCount;
    private int dislikeCount;
    private boolean isUpdated;
}
