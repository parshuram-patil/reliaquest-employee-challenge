package com.reliaquest.api.service;

import com.reliaquest.api.dto.CreateEmployeeRequestDto;
import com.reliaquest.api.dto.DeleteEmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.dto.EmployeeResponseDto;
import com.reliaquest.api.exception.EmployeeChallengeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
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

    @Value("${employee.data.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public List<EmployeeEntity> getAllEmployees() {
        ParameterizedTypeReference<EmployeeResponseDto<List<EmployeeEntity>>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<EmployeeResponseDto<List<EmployeeEntity>>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, responseType);

        return Objects.requireNonNull(response.getBody()).data();
    }

    public EmployeeEntity getEmployee(UUID id) {
        ParameterizedTypeReference<EmployeeResponseDto<EmployeeEntity>> responseType = new ParameterizedTypeReference<>() {};
        try {
            ResponseEntity<EmployeeResponseDto<EmployeeEntity>> response = restTemplate.exchange((apiUrl + "/" + id), HttpMethod.GET, null, responseType);
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

    public EmployeeEntity createEmployee(CreateEmployeeRequestDto employeeRequest) {
        ParameterizedTypeReference<EmployeeResponseDto<EmployeeEntity>> responseType = new ParameterizedTypeReference<>() {};
        HttpEntity<CreateEmployeeRequestDto> entity = new HttpEntity<>(employeeRequest);
        ResponseEntity<EmployeeResponseDto<EmployeeEntity>> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, responseType);
        EmployeeResponseDto<EmployeeEntity> responseDto = Objects.requireNonNull(response.getBody());

        return responseDto.data();
    }

    public Boolean deleteEmployee(DeleteEmployeeRequestDto employeeRequest) {
        ParameterizedTypeReference<EmployeeResponseDto<Boolean>> responseType = new ParameterizedTypeReference<>() {};
        HttpEntity<DeleteEmployeeRequestDto> entity = new HttpEntity<>(employeeRequest);
        ResponseEntity<EmployeeResponseDto<Boolean>> response = restTemplate.exchange(apiUrl, HttpMethod.DELETE, entity, responseType);
        EmployeeResponseDto<Boolean> responseDto = Objects.requireNonNull(response.getBody());
        Boolean result = responseDto.data();
        if(!result) {
            String errMsg = String.format("Employee with name %s not found", employeeRequest.name());
            log.error(errMsg);
            throw new EmployeeChallengeException(errMsg, HttpStatus.NOT_FOUND);
        }

        return true;
    }
}
