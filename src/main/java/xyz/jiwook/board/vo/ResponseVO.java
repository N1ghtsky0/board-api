package xyz.jiwook.board.vo;

import lombok.*;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class ResponseVO {
    private boolean success;
    private String message;
    private Object data;

    public static ResponseVO success() {
        return ResponseVO.builder().success(true).build();
    }

    public static ResponseVO success(String message) {
        return ResponseVO.builder().success(true).message(message).build();
    }

    public static ResponseVO success(Object data) {
        return ResponseVO.builder().success(true).data(data).build();
    }

    public static ResponseVO success(String message, Object data) {
        return ResponseVO.builder().success(true).message(message).data(data).build();
    }

    public static ResponseVO fail() {
        return ResponseVO.builder().success(false).build();
    }

    public static ResponseVO fail(String message) {
        return ResponseVO.builder().success(false).message(message).build();
    }

    public static ResponseVO fail(Object data) {
        return ResponseVO.builder().success(false).data(data).build();
    }

    public static ResponseVO fail(String message, Object data) {
        return ResponseVO.builder().success(false).message(message).data(data).build();
    }
}
