package ru.vw.practice.limits.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import ru.vw.practice.limits.exception.ValidationException;
import ru.vw.practice.limits.service.InputValidator;

import java.util.Set;

@Service
@AllArgsConstructor
public class InputValidatorImpl implements InputValidator {

  private final Validator validator;

  @Override
  public <T> void checkValidationErrors(T item) {
    Set<ConstraintViolation<T>> violations = validator.validate(item);

    if (!violations.isEmpty()) {
      throw new ValidationException(
              "Валидация входного параметра не пройдена",
              ValidationException.ErrorCodes.INVALID_INPUT,
              violations.stream()
                      .map(a->new ValidationException.ValidationFlagsDto(a.getPropertyPath().toString(), a.getMessage())).toList()
      );
    }
  }
}
