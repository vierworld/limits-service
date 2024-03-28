package ru.vw.practice.limits.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public interface OperationDto {
  @Data
  class RequestExecuteOperationDto {
    @DecimalMin(value = "0.0", inclusive = false, message = "идентификатор должен быть строго больше нуля")
    @NotNull(message = "должен быть заполнен для проведения операции")
    private Long clientId;

    @DecimalMin(value = "0.0", inclusive = false, message = "должен быть строго больше нуля")
    @Digits(integer=30, fraction=2, message = "должен иметь не больше 30 знаков в целой части и не больше 2-х в дробной части")
    @NotNull(message = "должен быть заполнен для проведения операции")
    private BigDecimal amount;
  }

  @Data
  @Builder
  class ResponseExecuteOperationDto {
    private BigDecimal availableLimit;
  }

  @Data
  class RequestRollbackOperationDto {
    @DecimalMin(value = "0.0", inclusive = false, message = "идентификатор должен быть строго больше нуля")
    @NotNull(message = "должен быть заполнен для проведения операции")
    private Long operationId;
  }

  @Data
  @Builder
  class ResponseRollbackOperationDto {
    private BigDecimal availableLimit;
  }

  @Data
  class RequestOperationInfoDto {
    @DecimalMin(value = "0.0", inclusive = false, message = "идентификатор должен быть строго больше нуля")
    @NotNull(message = "должен быть заполнен для проведения операции")
    private Long clientId;
    @NotNull(message = "должен быть заполнен для проведения операции")
    OffsetDateTime fromDate;
  }

  @Data
  @Builder
  class ResponseOperationInfoDto {
    private List<OperationInfoDto> operations;
  }

  @Data
  @Builder
  class OperationInfoDto {
    private Long operationId;
    private OffsetDateTime operationDate;
    private BigDecimal consumed;
    private Boolean rmv;

    @JsonIgnore
    public boolean isRemoved() {
      return !Objects.isNull(rmv) && rmv;
    }
  }

}
