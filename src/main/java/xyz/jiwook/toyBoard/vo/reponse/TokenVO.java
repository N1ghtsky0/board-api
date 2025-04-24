package xyz.jiwook.toyBoard.vo.reponse;

import jakarta.annotation.Nullable;

public record TokenVO(
        @Nullable String accessToken,
        @Nullable String refreshToken) {
}
