package xyz.jiwook.board.vo;

import lombok.Data;

@Data
public class UsernamePasswordVO {
    private String username;
    private String password;
    private String confirmPassword;
    private String currentPassword;
}
