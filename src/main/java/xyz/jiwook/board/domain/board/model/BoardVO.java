package xyz.jiwook.board.domain.board.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardVO {
    private long seq;
    private String title;
    private String content;
    private String author;
}
