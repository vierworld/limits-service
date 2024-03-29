package ru.vw.practice.limits.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidationException extends RuntimeException{
  private String errorMessage;
  private ErrorCodes code;
  private List<ValidationFlagsDto> validationFlags;

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  public static class ValidationFlagsDto {

    /** Наименование поля в json */
    private String fieldName;

    /** Сообщение об ошибке */
    private String hintText;
  }

  @Getter
  public enum ErrorCodes {
    NOT_FOUND(HttpStatus.NOT_FOUND),
    NOT_ENOUGH_RESOURCES(HttpStatus.CONFLICT),
    INVALID_INPUT(HttpStatus.UNPROCESSABLE_ENTITY);

    private final HttpStatus httpStatus;
    ErrorCodes(HttpStatus httpStatus) {
      this.httpStatus = httpStatus;
    }
  }
}
