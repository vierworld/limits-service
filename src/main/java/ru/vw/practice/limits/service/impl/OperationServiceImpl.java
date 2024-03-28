package ru.vw.practice.limits.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.vw.practice.limits.dto.OperationDto;
import ru.vw.practice.limits.entity.OperationEntity;
import ru.vw.practice.limits.exception.BusinessLogicException;
import ru.vw.practice.limits.repository.OperationsRepository;
import ru.vw.practice.limits.service.LimitService;
import ru.vw.practice.limits.service.OperationService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OperationServiceImpl implements OperationService {

  private final OperationsRepository operationsRepository;
  private final LimitService limitService;

  @Override
  @Transactional
  public OperationDto.ResponseOperationInfoDto getOperations(OperationDto.RequestOperationInfoDto request) {
    List<OperationEntity> result = operationsRepository.getByClientIdAndOpStampGreaterThan(request.getClientId(), request.getFromDate());
    return OperationDto.ResponseOperationInfoDto.builder().operations(result.stream().map(
            a -> OperationDto.OperationInfoDto.builder()
                    .operationDate(a.getOpStamp())
                    .operationId(a.getId())
                    .rmv(a.getRmv())
                    .consumed(a.getConsumed())
                    .build()
    ).toList()).build();
  }

  @Override
  @Transactional
  public OperationDto.OperationInfoDto getOperationById(long operationId) {
    OperationEntity result = operationsRepository.getById(operationId);
    return OperationDto.OperationInfoDto.builder()
            .operationDate(result.getOpStamp())
            .operationId(result.getId())
            .rmv(result.getRmv())
            .consumed(result.getConsumed())
            .build();
  }

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public OperationDto.ResponseExecuteOperationDto executeOperation(OperationDto.RequestExecuteOperationDto request) {
    OffsetDateTime operationDate = OffsetDateTime.now();
    BigDecimal result = limitService.consumeAndSave(request.getClientId(), request.getAmount(), operationDate);

    operationsRepository.save(OperationEntity.builder()
            .clientId(request.getClientId())
            .rmv(false)
            .opStamp(operationDate)
            .consumed(request.getAmount())
            .build());

    return OperationDto.ResponseExecuteOperationDto.builder().availableLimit(result).build();
  }

  @Override
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public OperationDto.ResponseRollbackOperationDto rollbackOperation(OperationDto.RequestRollbackOperationDto request) {
    OffsetDateTime operationDate = OffsetDateTime.now();
    OperationEntity operationEntity = operationsRepository.getById(request.getOperationId());
    if (Objects.isNull(operationEntity) || !Objects.isNull(operationEntity.getRmv()) && operationEntity.getRmv()) {
      throw new BusinessLogicException("Не обнаружена операция для отмены", BusinessLogicException.ExceptionCodes.OPERATION_IS_NOT_ACTIVE);
    }
    BigDecimal result = limitService.rollbackAndSave(operationEntity.getClientId(), operationEntity.getConsumed(), operationDate);
    operationEntity.setRmv(true);
    operationsRepository.save(operationEntity);
    return OperationDto.ResponseRollbackOperationDto.builder().availableLimit(result).build();
  }
}
