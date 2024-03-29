package ru.vw.practice.limits.service;

import ru.vw.practice.limits.dto.LimitDto;
import ru.vw.practice.limits.dto.MaintenanceDto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public interface LimitService {

  /**
   * Функция запроса информации по лимиту.
   *
   * @param clientId - id лимита
   * @return - ответ
   */
  LimitDto.ResponseLimitInfoDto getLimitData(long clientId);

  /**
   * Функция установки нового лимита.
   *
   * @param request - запрос
   * @return - ответ
   */
  MaintenanceDto.ResponseSetNewLimitDto setLimit(MaintenanceDto.RequestSetNewLimitDto request);

  /**
   * Функция потребления лимита.
   *
   * @param clientId - id клиента
   * @param consumed - количество на потребленние
   * @param operationDate - дата начала операции
   *
   * @return - остаток
   */
  BigDecimal consumeAndSave(long clientId, BigDecimal consumed, OffsetDateTime operationDate);

  /**
   * Функция отката потребления лимита.
   *
   * @param clientId - id клиента
   * @param consumed - количество на откат
   * @param operationDate - дата начала операции
   *
   * @return - остаток
   */
  BigDecimal rollbackAndSave(long clientId, BigDecimal consumed, OffsetDateTime operationDate);

}
