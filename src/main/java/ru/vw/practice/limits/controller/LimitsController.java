package ru.vw.practice.limits.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vw.practice.limits.dto.LimitDto;
import ru.vw.practice.limits.dto.MaintenanceDto;
import ru.vw.practice.limits.service.InputValidator;
import ru.vw.practice.limits.service.LimitService;

@RestController
@RequestMapping("api/v1/limit")
@AllArgsConstructor
public class LimitsController {
  private final LimitService limitsService;
  private final InputValidator validator;

  @GetMapping("/{clientId}")
  public LimitDto.ResponseLimitInfoDto getLimitByClientId(@PathVariable
                                                          long clientId) {
    return limitsService.getLimitData(clientId);
  }

  @PostMapping("/setup")
  public MaintenanceDto.ResponseSetNewLimitDto applyNewLimitValue(@RequestBody
                                                                  MaintenanceDto.RequestSetNewLimitDto request) {
    validator.checkValidationErrors(request);
    return limitsService.setLimit(request);
  }

}
