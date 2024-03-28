package ru.vw.practice.limits.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface LimitDto {

  @Data
  @Builder
  class ResponseLimitInfoDto {
    private BigDecimal limitConsumed;
    private BigDecimal limitTotal;
    private OffsetDateTime lastConsumedAt;
  }
}
