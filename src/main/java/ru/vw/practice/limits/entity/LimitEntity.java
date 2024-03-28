package ru.vw.practice.limits.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Entity
@Table(name = "cl_limit", schema = "csep")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LimitEntity {
  @Id
  @Column(name = "client_id")
  private Long clientId;

  @Column(name = "consumed")
  private BigDecimal consumed;

  @Column(name = "op_stamp")
  private OffsetDateTime opStamp;

  @Column(name = "daily_limit")
  private BigDecimal dailyLimit;

}
