package xyz.jiwook.toyBoard.config.exceptionConfig;

public class CustomInvalidJwtException extends RuntimeException {
    public CustomInvalidJwtException(String message) { super(message); }
}
