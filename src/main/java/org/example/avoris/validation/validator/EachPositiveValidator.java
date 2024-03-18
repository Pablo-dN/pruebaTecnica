package org.example.avoris.validation.validator;

import java.util.List;
import org.example.avoris.validation.annotation.EachPositive;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EachPositiveValidator implements ConstraintValidator<EachPositive, List<Integer>> {

  @Override
  public void initialize(EachPositive constraintAnnotation) {
  }

  @Override
  public boolean isValid(List<Integer> value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    for (Integer age : value) {
      if (age == null || age <= 0) {
        return false;
      }
    }

    return true;
  }
}
