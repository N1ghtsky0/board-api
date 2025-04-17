package xyz.jiwook.board.vo;

import lombok.Data;

@Data
public class TokenVO {
    private String accessToken;
    private String refreshToken;
}
