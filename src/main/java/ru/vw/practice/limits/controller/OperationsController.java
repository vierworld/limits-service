package ru.vw.practice.limits.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vw.practice.limits.dto.OperationDto;
import ru.vw.practice.limits.service.InputValidator;
import ru.vw.practice.limits.service.OperationService;


@RestController
@RequestMapping("api/v1/operation")
@AllArgsConstructor
public class OperationsController {
  private final OperationService operationService;
  private final InputValidator validator;

  @PostMapping("/exec")
  public OperationDto.ResponseExecuteOperationDto executeOperation(@RequestBody OperationDto.RequestExecuteOperationDto request) {
    validator.checkValidationErrors(request);
    return operationService.executeOperation(request);
  }

  @PostMapping("/rollback")
  public OperationDto.ResponseRollbackOperationDto executeOperation(@RequestBody OperationDto.RequestRollbackOperationDto request) {
    validator.checkValidationErrors(request);
    return operationService.rollbackOperation(request);
  }

  @PostMapping("/info")
  public OperationDto.ResponseOperationInfoDto getInfo(@RequestBody OperationDto.RequestOperationInfoDto request) {
    validator.checkValidationErrors(request);
    return operationService.getOperations(request);
  }

  @GetMapping("/{operationId}")
  public OperationDto.OperationInfoDto getOperation(@PathVariable long operationId) {
    return operationService.getOperationById(operationId);
  }

}
