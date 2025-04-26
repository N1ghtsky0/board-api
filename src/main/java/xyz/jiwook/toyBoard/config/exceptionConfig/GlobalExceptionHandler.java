package xyz.jiwook.toyBoard.config.exceptionConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("Business Exception: {{}}, message: {}", errorCode.toString(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorCode.toResponseBody());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurityException(SecurityException e) {
        return new ResponseEntity<>(e.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException (HttpServletRequest request, ConstraintViolationException  e) {
        String errorMessage = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toString();
        log.error("ConstraintViolationException : {} / {}", request.getRequestURI(), errorMessage);
        return new ResponseEntity<>(errorMessage, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {} / {}", request.getRequestURI(), e.getMessage());
        return new ResponseEntity<>(e.getBindingResult().getAllErrors().getFirst().getDefaultMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, Exception e) {
        log.error("Exception: {} / {} / {}", request.getRequestURI(), e.getClass(), e.getMessage());
        return new ResponseEntity<>(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
