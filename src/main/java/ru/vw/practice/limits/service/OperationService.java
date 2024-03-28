package ru.vw.practice.limits.service;

import ru.vw.practice.limits.dto.OperationDto;

public interface OperationService {
  /**
   * Функция получения списка операций.
   *
   * @param request - запрос
   * @return - ответ
   */
  OperationDto.ResponseOperationInfoDto getOperations(OperationDto.RequestOperationInfoDto request);

  /**
   * Функция получения списка операций.
   *
   * @param operationId - id операции
   * @return - информация об операции
   */
  OperationDto.OperationInfoDto getOperationById(long operationId);



  /**
   * Функция выполнения операции.
   *
   * @param request - запрос
   * @return - ответ
   */
  OperationDto.ResponseExecuteOperationDto executeOperation(OperationDto.RequestExecuteOperationDto request);


  /**
   * Функция отката операции.
   *
   * @param request - запрос
   * @return - ответ
   */
  OperationDto.ResponseRollbackOperationDto rollbackOperation(OperationDto.RequestRollbackOperationDto request);

}
