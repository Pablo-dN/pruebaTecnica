package org.example.avoris.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.example.avoris.validation.validator.EachPositiveValidator;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EachPositiveValidator.class)
public @interface EachPositive {
  String message() default "Each element must be positive";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
