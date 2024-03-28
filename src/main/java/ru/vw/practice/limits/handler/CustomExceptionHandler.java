package ru.vw.practice.limits.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.vw.practice.limits.dto.ExceptionsDto;
import ru.vw.practice.limits.exception.BusinessLogicException;
import ru.vw.practice.limits.exception.ValidationException;

import java.time.OffsetDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

  @ExceptionHandler(BusinessLogicException.class)
  public ResponseEntity<ExceptionsDto.BusinessExceptionDto> handleCustomDetailedException(BusinessLogicException e) {

    return new ResponseEntity<ExceptionsDto.BusinessExceptionDto>(
            ExceptionsDto.BusinessExceptionDto.builder()
                    .errorCode(e.getErrorCode().toString())
                    .errorDateTime(OffsetDateTime.now())
                    .errorMessage(e.getErrorText())
                    .build(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ExceptionsDto.ValidationExceptionDto> handleCustomDetailedException(ValidationException e) {

    return new ResponseEntity<>(ExceptionsDto.ValidationExceptionDto.builder()
            .errorMessage(e.getErrorMessage())
            .code(e.getCode())
            .validationFlags(e.getValidationFlags())
            .build(), HttpStatus.UNPROCESSABLE_ENTITY);
  }

}