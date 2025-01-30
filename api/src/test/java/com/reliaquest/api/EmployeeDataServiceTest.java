package com.reliaquest.api;

import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.dto.EmployeeResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeDataServiceTest {

    @InjectMocks
    EmployeeDataService service;

    @Mock
    RestTemplate restTemplate;

    @Test
    void shouldCreate() {
        assertNotNull(service);
    }

    @Test
    void shouldGetAllEmployees() {
        EmployeeResponseDto<List<EmployeeEntity>> responseDto = new EmployeeResponseDto<>(getMockedEmployees(), "ACK", null);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), eq(null), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        List<EmployeeEntity> allEmployees = service.getAllEmployees();

        assertEquals(3, allEmployees.size());
        ParameterizedTypeReference<EmployeeResponseDto<List<EmployeeEntity>>> responseType =
                new ParameterizedTypeReference<>() {};
        verify(restTemplate).exchange("http://localhost:8112/api/v1/employee", HttpMethod.GET, null, responseType);
    }

    @Test
    void shouldGetNoEmployees() {
        EmployeeResponseDto<List<EmployeeEntity>> responseDto = new EmployeeResponseDto<>(List.of(), "ACK", null);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), eq(null), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        List<EmployeeEntity> allEmployees = service.getAllEmployees();

        assertEquals(0, allEmployees.size());
    }

    private List<EmployeeEntity> getMockedEmployees() {
        return List.of(
                new EmployeeEntity("test-1", "John Doe", 50000, 30, "Engineer", "john@tets.com"),
                new EmployeeEntity("test-2", "Jane Smith", 60000, 35, "Manager", "jane@test.com"),
                new EmployeeEntity("test-3", "Bob Johnson", 45000, 35, "Developer", "bob@test.com"
                )
        );
    }

}