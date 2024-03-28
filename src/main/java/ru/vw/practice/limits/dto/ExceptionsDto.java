package ru.vw.practice.limits.dto;

import lombok.Builder;
import lombok.Data;
import ru.vw.practice.limits.exception.ValidationException;

import java.time.OffsetDateTime;
import java.util.List;

public interface ExceptionsDto {
  @Data
  @Builder
  class BusinessExceptionDto {
    private String errorCode;
    private String errorMessage;
    private OffsetDateTime errorDateTime;
  }

  @Data
  @Builder
  class ValidationExceptionDto {
    private String errorMessage;
    private ValidationException.ErrorCodes code;
    private List<ValidationException.ValidationFlagsDto> validationFlags;
  }


}
