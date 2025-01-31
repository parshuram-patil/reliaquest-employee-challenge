package com.reliaquest.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.UUID;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // Apply snake_case strategy
public record EmployeeEntity(
        UUID id,
        String employeeName,
        Integer employeeSalary,
        Integer employeeAge,
        String employeeTitle,
        String employeeEmail
) {}
