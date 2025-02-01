package com.reliaquest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.CreateEmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.dto.EmployeeResponseDto;
import com.reliaquest.api.exception.EmployeeChallengeException;
import com.reliaquest.api.exception.GlobalExceptionHandler;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EmployeeService employeeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new EmployeeController(employeeService))
                .setControllerAdvice(new GlobalExceptionHandler(objectMapper))
                .build();
    }

    @Test
    void testGetAllEmployees() throws Exception {
        EmployeeEntity emp1 = new EmployeeEntity(UUID.randomUUID(), "John Doe", 50000, 30, "Engineer", "john@tets.com");
        EmployeeEntity emp2 = new EmployeeEntity(UUID.randomUUID(), "Jane Smith", 60000, 35, "Manager", "jane@test.com");
        List<EmployeeEntity> employees = List.of(emp1, emp2);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetEmployeesByNameSearch() throws Exception {
        EmployeeEntity employee = new EmployeeEntity(UUID.randomUUID(), "John Doe", 10000, 22, "Trainee", "john@tets.com");
        List<EmployeeEntity> employees = List.of(employee);

        when(employeeService.getEmployeesByNameSearch("John")).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employee/search/John"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        verify(employeeService, times(1)).getEmployeesByNameSearch("John");
    }

    @Test
    void testGetEmployeeById() throws Exception {
        UUID id = UUID.randomUUID();
        EmployeeEntity employee = new EmployeeEntity(UUID.randomUUID(), "John Doe", 10000, 22, "Trainee", "john@tets.com");
        when(employeeService.getEmployee(id)).thenReturn(employee);

        mockMvc.perform(get("/api/v1/employee/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.salary").value(10000))
                .andExpect(jsonPath("$.age").value(22))
                .andExpect(jsonPath("$.title").value("Trainee"))
                .andExpect(jsonPath("$.email").value("john@tets.com"));

        verify(employeeService, times(1)).getEmployee(id);
    }

    @Test
    void testGetHighestSalaryOfEmployees() throws Exception {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(1500);

        mockMvc.perform(get("/api/v1/employee/highestSalary"))
                .andExpect(status().isOk())
                .andExpect(content().string("1500"));

        verify(employeeService, times(1)).getHighestSalaryOfEmployees();
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> topEmployees = Arrays.asList("John Doe", "Jane Doe");

        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(topEmployees);

        mockMvc.perform(get("/api/v1/employee/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("John Doe"))
                .andExpect(jsonPath("$[1]").value("Jane Doe"));

        verify(employeeService, times(1)).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeEntity employee = new EmployeeEntity(UUID.randomUUID(), "John Doe", 10000, 22, "Trainee", "john@tets.com");
        CreateEmployeeRequestDto requestDto = new CreateEmployeeRequestDto(employee.getName(), employee.getSalary(), employee.getAge(), employee.getTitle());

        when(employeeService.createEmployee(requestDto)).thenReturn(employee);

        mockMvc.perform(post("/api/v1/employee")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.salary").value(10000));

        verify(employeeService, times(1)).createEmployee(requestDto);
    }

    @Test
    void testDeleteEmployeeByName() throws Exception {
        String name = "John Doe";
        when(employeeService.deleteEmployee(name)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/employee/" + name))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(employeeService, times(1)).deleteEmployee(name);
    }

    @Test
    public void shouldEmployeeChallengeExceptionException() throws Exception {
        when(employeeService.deleteEmployee("Test")).thenThrow(new EmployeeChallengeException("Employee Not Found", HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/api/v1/employee/Test"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee Not Found"))
                .andReturn();
    }

    @Test
    public void shouldHandleTooManyRequestsException() throws Exception {
        HttpClientErrorException httpClientErrorException = HttpClientErrorException.create(HttpStatusCode.valueOf(429), "Too Many Requests", null, null, StandardCharsets.UTF_8);
        when(employeeService.getAllEmployees()).thenThrow(httpClientErrorException);

        mockMvc.perform(get("/api/v1/employee"))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.message").value("Too many requests, Please try again later"))
                .andReturn();
    }

    @Test
    public void shouldHandleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get("/api/v1/employee/xyz"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid UUID string: xyz"))
                .andReturn();
    }

    @Test
    public void shouldHandleClientErrorException() throws Exception {
        EmployeeResponseDto<String> responseDto = new EmployeeResponseDto<>(null, "Bad Request", "Bad Request");
        when(employeeService.getAllEmployees()).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", objectMapper.writeValueAsBytes(responseDto), StandardCharsets.UTF_8));

        mockMvc.perform(get("/api/v1/employee"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad Request"))
                .andReturn();
    }

    @Test
    public void shouldHandleServerErrorException() throws Exception {
        String statusText = "Not Implemented";
        EmployeeResponseDto<String> responseDto = new EmployeeResponseDto<>(null, statusText, statusText);
        when(employeeService.getAllEmployees()).thenThrow(new HttpServerErrorException(HttpStatus.NOT_IMPLEMENTED, statusText, objectMapper.writeValueAsBytes(responseDto), StandardCharsets.UTF_8));

        mockMvc.perform(get("/api/v1/employee"))
                .andExpect(status().isNotImplemented())
                .andExpect(jsonPath("$.message").value("Not Implemented"))
                .andReturn();
    }

    @Test
    public void shouldHandleResourceAccessException() throws Exception {
        when(employeeService.getAllEmployees()).thenThrow(new ResourceAccessException("Network Error"));

        mockMvc.perform(get("/api/v1/employee"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.message").value("Employee data service is unavailable"))
                .andReturn();
    }

    @Test
    public void shouldHandleUnhandledException() throws Exception {
        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("Any Error"));

        mockMvc.perform(get("/api/v1/employee"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Any Error"))
                .andReturn();
    }

}