package org.example.avoris.validation.validator;

import org.example.avoris.dto.SearchRequest;
import org.example.avoris.validation.annotation.CheckInCheckOut;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckInCheckOutValidator implements ConstraintValidator<CheckInCheckOut, SearchRequest> {

  @Override
  public void initialize(CheckInCheckOut constraintAnnotation) {
  }

  @Override
  public boolean isValid(SearchRequest request, ConstraintValidatorContext context) {
    if (request == null) {
      return true;
    }

    return request.getCheckIn() == null || request.getCheckOut() == null ||
        request.getCheckIn().isBefore(request.getCheckOut());
  }
}
