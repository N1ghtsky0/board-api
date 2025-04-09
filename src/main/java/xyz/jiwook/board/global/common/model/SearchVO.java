package xyz.jiwook.board.global.common.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SearchVO {
    private String keyword = "";
    private String category = "";
}
