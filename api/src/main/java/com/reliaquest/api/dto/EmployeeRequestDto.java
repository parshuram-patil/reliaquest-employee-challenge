package com.reliaquest.api.dto;

public record EmployeeRequestDto (
    String name,
    Integer salary,
    Integer age,
    String title
){}
