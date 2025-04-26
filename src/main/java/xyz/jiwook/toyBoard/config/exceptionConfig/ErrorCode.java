package xyz.jiwook.toyBoard.config.exceptionConfig;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {
    INVALID_ACCESS_TOKEN(BAD_REQUEST, "T0010", "유효하지 않은 토큰입니다"),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "T0020", "유효하지 않은 토큰입니다."),

    EXPIRED_TOKEN(OK, "T0011", "토큰이 만료되었습니다."),

    LOGIN_FAIL_USERNAME_NOT_FOUND(OK, "A0010", "존재하지 않는 아이디 이거나 비밀번호가 틀렸습니다."),
    LOGIN_FAIL_WRONG_PASSWORD(OK, "A0010", "존재하지 않는 아이디 이거나 비밀번호가 틀렸습니다."),
    LOGIN_FAIL_IGNORED_USERNAME(OK, "A0011", "사용 불가능한 계정입니다."),
    LOGIN_FAIL_TOO_MANY_FAIL(OK, "A0012", "로그인에 5회 이상 실패하여 임시 잠금 처리된 계정입니다."),

    NO_AUTHORITY(BAD_REQUEST, "S0010", "수정할 수 있는 권한이 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }


    @Override
    public String toString() {
        return "[" + status.value() + "] " + code + " : " + message;
    }

    public HashMap<String, String> toResponseBody() {
        return new HashMap<>() {{
            put("code", code);
            put("message", message);
        }};
    }
}
