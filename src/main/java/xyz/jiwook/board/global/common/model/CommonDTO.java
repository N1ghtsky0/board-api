package xyz.jiwook.board.global.common.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommonDTO {
    private boolean success;
    private String message;
    private Object data;

    public static CommonDTO success() {
        return CommonDTO.builder().success(true).build();
    }

    public static CommonDTO success(Object data) {
        return CommonDTO.builder().success(true).data(data).build();
    }

    public static CommonDTO success(String message) {
        return CommonDTO.builder().success(true).message(message).build();
    }

    public static CommonDTO success(String message, Object data) {
        return CommonDTO.builder().success(true).message(message).data(data).build();
    }

    public static CommonDTO fail() {
        return CommonDTO.builder().success(false).build();
    }

    public static CommonDTO fail(String message) {
        return CommonDTO.builder().success(false).message(message).build();
    }

}
