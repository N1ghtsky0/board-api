package xyz.jiwook.toyBoard.config.exceptionConfig;

public class CustomLoginFailException extends RuntimeException {
    public CustomLoginFailException() {

    }

    public CustomLoginFailException(String message) {
        super(message);
    }
}
