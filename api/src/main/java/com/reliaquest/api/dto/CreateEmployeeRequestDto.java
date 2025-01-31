package com.reliaquest.api.dto;

public record CreateEmployeeRequestDto(
    String name,
    Integer salary,
    Integer age,
    String title
){}
