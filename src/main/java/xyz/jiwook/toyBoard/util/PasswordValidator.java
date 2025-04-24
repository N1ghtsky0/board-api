package xyz.jiwook.toyBoard.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호는 필수입니다.").addConstraintViolation();
            return false;
        }
        if (!password.matches("^\\S+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호에는 공백이 포함될 수 없습니다.").addConstraintViolation();
            return false;
        }
        if (password.length() < 8 || password.length() > 20) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("비밀번호는 8자 이상 20자 이하로 입력하세요.").addConstraintViolation();
            return false;
        }
        return true;
    }
}
