package com.blogapp.bloggingapplication.Services;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Payload;
import jakarta.validation.Constraint;
@Constraint(validatedBy = GmailValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGmail {
    String message() default "Email must be a valid Gmail address.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
