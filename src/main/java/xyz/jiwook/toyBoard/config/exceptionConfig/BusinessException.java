package xyz.jiwook.toyBoard.config.exceptionConfig;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) { this.errorCode = errorCode; }
    public BusinessException(ErrorCode errorCode, String loggingMessage) { super(loggingMessage); this.errorCode = errorCode; }
}
