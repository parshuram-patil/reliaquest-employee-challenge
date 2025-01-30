package com.reliaquest.api.dto;

public record EmployeeResponseDto(
        EmployeeEntity data,
        String status,
        String error
) {}
