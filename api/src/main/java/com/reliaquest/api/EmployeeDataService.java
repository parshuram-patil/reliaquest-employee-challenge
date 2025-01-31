package com.reliaquest.api;

import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.dto.EmployeeResponseDto;
import com.reliaquest.api.exception.EmployeeChallengeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeDataService {
    private final RestTemplate restTemplate;
    private final String API_URL = "http://localhost:8112/api/v1/employee";

    public List<EmployeeEntity> getAllEmployees() {
        ParameterizedTypeReference<EmployeeResponseDto<List<EmployeeEntity>>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<EmployeeResponseDto<List<EmployeeEntity>>> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, responseType);

        return Objects.requireNonNull(response.getBody()).data();
    }

    public EmployeeEntity getEmployee(UUID id) {
        ParameterizedTypeReference<EmployeeResponseDto<EmployeeEntity>> responseType = new ParameterizedTypeReference<>() {};
        try {
            ResponseEntity<EmployeeResponseDto<EmployeeEntity>> response = restTemplate.exchange((API_URL + "/" + id), HttpMethod.GET, null, responseType);
            EmployeeResponseDto<EmployeeEntity> responseDto = Objects.requireNonNull(response.getBody());
            return responseDto.data();
        } catch (HttpClientErrorException ex) {
            if(ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                String errMsg = String.format("Employee with id %s not found", id);
                log.error(errMsg, ex);
                throw new EmployeeChallengeException(errMsg, HttpStatus.NOT_FOUND);
            } else {
                String errMsg = String.format("Error fetching employee with id %s", id);
                log.error(errMsg, ex);
                throw new EmployeeChallengeException(errMsg);
            }
        }
    }
}
