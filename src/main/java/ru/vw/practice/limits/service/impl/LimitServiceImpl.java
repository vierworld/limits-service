package ru.vw.practice.limits.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.vw.practice.limits.configuration.LimitConfiguration;
import ru.vw.practice.limits.dto.LimitDto;
import ru.vw.practice.limits.dto.MaintenanceDto;
import ru.vw.practice.limits.entity.LimitEntity;
import ru.vw.practice.limits.exception.BusinessLogicException;
import ru.vw.practice.limits.repository.LimitsRepository;
import ru.vw.practice.limits.service.LimitService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@AllArgsConstructor
public class LimitServiceImpl implements LimitService {
  private final LimitsRepository limitsRepository;
  private final LimitConfiguration limitConfiguration;

  @Override
  @Transactional
  public LimitDto.ResponseLimitInfoDto getLimitData(long clientId) {
    LimitEntity limitEntity = getLimitEntityOrDefault(clientId);
    return LimitDto.ResponseLimitInfoDto.builder()
            .limitTotal(limitEntity.getDailyLimit())
            .limitConsumed(limitEntity.getConsumed())
            .lastConsumedAt(limitEntity.getOpStamp())
            .build();
  }

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public MaintenanceDto.ResponseSetNewLimitDto setLimit(MaintenanceDto.RequestSetNewLimitDto request) {
    LimitEntity limitEntity = getLimitEntityOrDefault(request.getClientId());

    if (limitEntity.getOpStamp().atZoneSameInstant(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS)
            .isBefore(OffsetDateTime.now().atZoneSameInstant(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS))) {
      limitEntity.setConsumed(BigDecimal.ZERO);
    }

    if (limitEntity.getConsumed().compareTo(request.getLimit()) > 0 && !request.checkIsForced()) {
      throw new BusinessLogicException("Текущий потребленный лимит: %s, новое значение не должно быть меньше %s (либо используйте флаг forced)"
              .formatted(limitEntity.getConsumed(), request.getLimit()),
              BusinessLogicException.ExceptionCodes.NEW_LIMIT_IS_TOO_LOW);
    }

    limitEntity.setDailyLimit(request.getLimit());
    limitsRepository.save(limitEntity);
    return MaintenanceDto.ResponseSetNewLimitDto.builder()
            .availableLimit(limitEntity.getDailyLimit().subtract(limitEntity.getConsumed()))
            .newLimit(limitEntity.getDailyLimit())
            .build();
  }

  private LimitEntity getLimitEntityOrDefault(long clientId) {
    LimitEntity limitEntity = limitsRepository.getByClientId(clientId);
    if (Objects.isNull(limitEntity)) {
      return LimitEntity.builder()
              .clientId(clientId)
              .dailyLimit(limitConfiguration.getDefaultLimit())
              .consumed(BigDecimal.ZERO)
              .opStamp(OffsetDateTime.now())
              .build();
    }
    return limitEntity;
  }


  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public BigDecimal consumeAndSave(long clientId, BigDecimal consumed, OffsetDateTime operationDate) {
    LimitEntity limitEntity = getLimitEntityOrDefault(clientId);

    if (limitEntity.getOpStamp().atZoneSameInstant(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS)
            .isBefore(OffsetDateTime.now().atZoneSameInstant(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS))) {
      limitEntity.setConsumed(BigDecimal.ZERO);
    }
    BigDecimal newConsumedValue = limitEntity.getConsumed().add(consumed);
    if (newConsumedValue.compareTo(limitEntity.getDailyLimit()) > 0) {
      throw new BusinessLogicException("Result %s is greater than %s".formatted(newConsumedValue, limitEntity.getDailyLimit()),
              BusinessLogicException.ExceptionCodes.LIMIT_EXCEEDED);
    }
    limitEntity.setConsumed(newConsumedValue);
    limitEntity.setOpStamp(operationDate);
    limitsRepository.save(limitEntity);

    return limitEntity.getDailyLimit().subtract(newConsumedValue);
  }

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public BigDecimal rollbackAndSave(long clientId, BigDecimal consumed, OffsetDateTime operationDate) {
    LimitEntity limitEntity = getLimitEntityOrDefault(clientId);
    if (limitEntity.getOpStamp().atZoneSameInstant(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS)
            .isBefore(OffsetDateTime.now().atZoneSameInstant(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS))) {
      return BigDecimal.ZERO;
    }

    BigDecimal newConsumedValue = limitEntity.getConsumed().subtract(consumed);

    if (newConsumedValue.compareTo(BigDecimal.ZERO) < 0) {
      throw new BusinessLogicException("После отмены операции сумма потребленного стала меньше нуля",
              BusinessLogicException.ExceptionCodes.CONSUMED_IS_NEGATIVE);
    }

    limitEntity.setConsumed(newConsumedValue);
    limitsRepository.save(limitEntity);

    return limitEntity.getDailyLimit().subtract(newConsumedValue);
  }

}
