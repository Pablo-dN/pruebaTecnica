package org.example.avoris.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.example.avoris.validation.validator.CheckInCheckOutValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckInCheckOutValidator.class)
public @interface CheckInCheckOut {
  String message() default "Check-in date must be before check-out date";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
