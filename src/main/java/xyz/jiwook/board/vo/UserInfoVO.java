package xyz.jiwook.board.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfoVO {
    private String kickName;
    private String thumbnail;
    private String introduce;
    private String regDt;
}
