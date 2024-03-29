package ru.vw.practice.limits.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "limit")
@Data
public class LimitConfiguration {
  private BigDecimal defaultLimit;
}
