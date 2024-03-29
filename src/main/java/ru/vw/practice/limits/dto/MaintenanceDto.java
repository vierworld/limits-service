package ru.vw.practice.limits.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

public interface MaintenanceDto {
  @Data
  class RequestSetNewLimitDto {
    @DecimalMin(value = "0.0", inclusive = false, message = "идентификатор должен быть строго больше нуля")
    @NotNull(message = "должен быть заполнен для проведения операции")
    private Long clientId;

    @DecimalMin(value = "0.0", inclusive = false, message = "должен быть строго больше нуля")
    @Digits(integer=30, fraction=2, message = "должен иметь не больше 30 знаков в целой части и не больше 2-х в дробной части")
    @NotNull(message = "должен быть заполнен для проведения операции")
    private BigDecimal limit;
    private Boolean forced;

    @JsonIgnore
    public boolean checkIsForced() {
      return !Objects.isNull(forced) && forced;
    }
  }
  @Data
  @Builder
  class ResponseSetNewLimitDto {
    private BigDecimal newLimit;
    private BigDecimal availableLimit;
  }

}
