package com.reliaquest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.CreateEmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        mockMvc = MockMvcBuilders.standaloneSetup(new EmployeeController(employeeService)).build();
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
                .andExpect(jsonPath("$.employee_name").value("John Doe"))
                .andExpect(jsonPath("$.employee_salary").value(10000));

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
        CreateEmployeeRequestDto requestDto = new CreateEmployeeRequestDto(employee.employeeName(), employee.employeeSalary(), employee.employeeAge(), employee.employeeTitle());

        when(employeeService.createEmployee(requestDto)).thenReturn(employee);

        mockMvc.perform(post("/api/v1/employee")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name").value("John Doe"))
                .andExpect(jsonPath("$.employee_salary").value(10000));

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

}