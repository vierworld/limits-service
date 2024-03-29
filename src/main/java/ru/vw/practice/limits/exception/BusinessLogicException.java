package ru.vw.practice.limits.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class BusinessLogicException extends RuntimeException {
  private final String errorText;
  private final ExceptionCodes errorCode;


  @AllArgsConstructor
  @Getter
  public enum ExceptionCodes {
    LIMIT_EXCEEDED("Превышен лимит"),
    OPERATION_IS_NOT_ACTIVE("Операция уже отменена"),
    CONSUMED_IS_NEGATIVE("Ошибка в вычислении лимита, потребленный лимит стал отрицателен"),
    NEW_LIMIT_IS_TOO_LOW("Новый лимит не может быть меньше уже потребленного");

    private final String text;
  }

}
