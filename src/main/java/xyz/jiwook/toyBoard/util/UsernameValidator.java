package xyz.jiwook.toyBoard.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("아이디는 필수입니다.").addConstraintViolation();
            return false;
        }
        if (!username.matches("^\\S+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("아이디에는 공백이 포함될 수 없습니다.").addConstraintViolation();
            return false;
        }
        if (username.length() < 8 || username.length() > 20) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("아이디는 8자 이상 20자 이하로 입력하세요.").addConstraintViolation();
            return false;
        }
        return true;
    }
}
