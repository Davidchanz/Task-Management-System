package com.effectivemobile.TaskManagementSystem.validation.annotation;

import com.effectivemobile.TaskManagementSystem.validation.UserNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserNameValidator.class)
@Documented
public @interface ValidUserName {
    String message() default """
            "Invalid UserName"
            "UserName must only contains alphanumeric characters, underscore and dot"
            "Underscore and dot can't be at the end or start of a username (e.g _username / username_ / .username / username.)"
            "Underscore and dot can't be next to each other (e.g user_.name)"
            "Underscore or dot can't be used multiple times in a row (e.g user__name / user..name)"
            "Number of characters must be between 8 to 20"
            """;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
