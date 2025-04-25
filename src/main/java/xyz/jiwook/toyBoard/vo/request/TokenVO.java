package xyz.jiwook.toyBoard.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenVO {
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
