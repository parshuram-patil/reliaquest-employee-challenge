package com.reliaquest.api.service;

import com.reliaquest.api.dto.CreateEmployeeRequestDto;
import com.reliaquest.api.dto.DeleteEmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.utils.EmployeeTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    EmployeeService service;

    @Mock
    EmployeeDataService dataService;

    @Test
    void shouldGetAllEmployees() {
        List<EmployeeEntity> expected = EmployeeTestUtil.getMockedEmployees().values().stream().toList();
        when(dataService.getAllEmployees()).thenReturn(expected);

        List<EmployeeEntity> actual = service.getAllEmployees();

        assertEquals(expected, actual);
        verify(dataService).getAllEmployees();
    }

    @Test
    void shouldGetEmployee() {
        EmployeeEntity expected = EmployeeTestUtil.getMockedEmployees().values().stream().findFirst().get();
        when(dataService.getEmployee(any())).thenReturn(expected);

        EmployeeEntity actual = service.getEmployee(expected.id());

        assertEquals(expected, actual);
        verify(dataService).getEmployee(expected.id());
    }

    @Test
    void shouldCreateEmployee() {
        CreateEmployeeRequestDto requestDto = new CreateEmployeeRequestDto("John Doe", 50000, 30, "Engineer");
        EmployeeEntity expected = EmployeeTestUtil.getMockedEmployees().values().stream().findFirst().get();
        when(dataService.createEmployee(any())).thenReturn(expected);

        EmployeeEntity actual = service.createEmployee(requestDto);

        assertEquals(expected, actual);
        verify(dataService).createEmployee(requestDto);
    }

    @Test
    void shouldDeleteEmployee() {
        DeleteEmployeeRequestDto requestDto = new DeleteEmployeeRequestDto("Bob Johnson");
        when(dataService.deleteEmployee(any())).thenReturn(true);

        Boolean actual = service.deleteEmployee("Bob Johnson");

        assertTrue(actual);
        verify(dataService).deleteEmployee(requestDto);
    }

    @Test
    void shouldNotDeleteEmployee() {
        when(dataService.deleteEmployee(any())).thenReturn(false);

        Boolean actual = service.deleteEmployee("XyZ");

        assertFalse(actual);
    }
}