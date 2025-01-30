package com.reliaquest.api;

import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.dto.EmployeeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeDataService {
    private final RestTemplate restTemplate;
    private final String API_URL = "http://localhost:8112/api/v1/employee";

    public List<EmployeeEntity> getAllEmployees() {
        ParameterizedTypeReference<EmployeeResponseDto<List<EmployeeEntity>>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<EmployeeResponseDto<List<EmployeeEntity>>> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, responseType);

        return Objects.requireNonNull(response.getBody()).data();
    }
}
