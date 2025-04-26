package xyz.jiwook.toyBoard.vo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ContentOnly(
        @JsonProperty("content")
        @NotBlank(message = "내용은 필수입니다.")
        String content
) {
}
