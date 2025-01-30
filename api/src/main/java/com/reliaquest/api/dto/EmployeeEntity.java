package com.reliaquest.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // Apply snake_case strategy
public record EmployeeEntity(
        String id,
        String employeeName,
        Integer employeeSalary,
        Integer employeeAge,
        String employeeTitle,
        String employeeEmail
) {}
