package com.reliaquest.api.dto;


public record EmployeeResponseDto<T>(
        T data,
        String status,
        String error
) {}
