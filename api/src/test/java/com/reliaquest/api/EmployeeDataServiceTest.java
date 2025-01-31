package com.reliaquest.api;

import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.dto.EmployeeResponseDto;
import com.reliaquest.api.exception.EmployeeChallengeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
        List<EmployeeEntity> empList = getMockedEmployees().values().stream().toList();
        EmployeeResponseDto<List<EmployeeEntity>> responseDto = new EmployeeResponseDto<>(empList, "ACK", null);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), eq(null), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        List<EmployeeEntity> allEmployees = service.getAllEmployees();

        assertEquals(3, allEmployees.size());
        ParameterizedTypeReference<EmployeeResponseDto<List<EmployeeEntity>>> responseType = new ParameterizedTypeReference<>() {};
        verify(restTemplate).exchange("http://localhost:8112/api/v1/employee", HttpMethod.GET, null, responseType);
    }

    @Test
    void shouldGetNoEmployees() {
        EmployeeResponseDto<List<EmployeeEntity>> responseDto = new EmployeeResponseDto<>(List.of(), "ACK", null);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), eq(null), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        List<EmployeeEntity> allEmployees = service.getAllEmployees();

        assertEquals(0, allEmployees.size());
    }

    @Test
    void shouldGetEmployee() {
        Map.Entry<UUID, EmployeeEntity> empEntry = getMockedEmployees().entrySet().iterator().next();
        EmployeeEntity emp = empEntry.getValue();
        UUID id = empEntry.getKey();
        EmployeeResponseDto<EmployeeEntity> responseDto = new EmployeeResponseDto<>(emp, "ACK", null);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), eq(null), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(responseDto, HttpStatus.OK));

        EmployeeEntity employee = service.getEmployee(id);

        assertEquals(id, employee.id());
        ParameterizedTypeReference<EmployeeResponseDto<EmployeeEntity>> responseType = new ParameterizedTypeReference<>() {};
        verify(restTemplate).exchange("http://localhost:8112/api/v1/employee/" + id, HttpMethod.GET, null, responseType);
    }

    @Test
    void shouldHandleNotFoundErrorOnGetEmployee() {
        UUID empId = UUID.randomUUID();
        EmployeeResponseDto<String> empNotFound = new EmployeeResponseDto<>(null, "Success", "Not Found");
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), eq(null), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        EmployeeChallengeException error = assertThrows(EmployeeChallengeException.class, () -> service.getEmployee(empId));
        assertEquals("Employee with id " + empId + " not found", error.getMessage());
    }

    @Test
    void shouldHandleErrorOnGetEmployee() {
        UUID empId = UUID.randomUUID();
        EmployeeResponseDto<String> empNotFound = new EmployeeResponseDto<>(null, "Success", "Not Found");
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), eq(null), any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE));

        EmployeeChallengeException error = assertThrows(EmployeeChallengeException.class, () -> service.getEmployee(empId));
        assertEquals("Error fetching employee with id " + empId, error.getMessage());
    }

    private Map<UUID, EmployeeEntity> getMockedEmployees() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();
        return Map.of(
                uuid1, new EmployeeEntity(uuid1, "John Doe", 50000, 30, "Engineer", "john@tets.com"),
                uuid2, new EmployeeEntity(uuid2, "Jane Smith", 60000, 35, "Manager", "jane@test.com"),
                uuid3, new EmployeeEntity(uuid3, "Bob Johnson", 45000, 35, "Developer", "bob@test.com")
        );
    }

}