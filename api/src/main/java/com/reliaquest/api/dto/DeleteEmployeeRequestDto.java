package com.reliaquest.api.dto;

import lombok.Builder;


@Builder
public record DeleteEmployeeRequestDto(
    String name
){}
