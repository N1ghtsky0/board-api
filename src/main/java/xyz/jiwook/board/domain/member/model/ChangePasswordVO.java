package xyz.jiwook.board.domain.member.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordVO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    public boolean validateNewPassword() {
        return newPassword.equals(confirmPassword);
    }
}
