package xyz.jiwook.board.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
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

    public PostVO(long id, String title, String content, String updDt, String author, String thumbnail, boolean isUpdated) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.updDt = updDt;
        this.author = author;
        this.thumbnail = thumbnail;
        this.isUpdated = isUpdated;
    }
}
