package com.effectivemobile.TaskManagementSystem.validation.annotation;

import com.effectivemobile.TaskManagementSystem.validation.PasswordValidator;
import com.effectivemobile.TaskManagementSystem.validation.UserNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default """
            "Invalid Password"
            "Password must be minimum 4 and maximum 10 characters or numbers"
            """;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
