package xyz.jiwook.board.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenVO {
    private String accessToken;
    private String refreshToken;
}
