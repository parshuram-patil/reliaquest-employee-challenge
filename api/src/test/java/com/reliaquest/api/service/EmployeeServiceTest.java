package com.reliaquest.api.service;

import com.reliaquest.api.dto.CreateEmployeeRequestDto;
import com.reliaquest.api.dto.DeleteEmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.exception.EmployeeChallengeException;
import com.reliaquest.api.utils.EmployeeTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

        EmployeeEntity actual = service.getEmployee(expected.getId());

        assertEquals(expected, actual);
        verify(dataService).getEmployee(expected.getId());
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

    @Test
    void shouldSearchEmployeeWithName() {
        List<EmployeeEntity> empList = EmployeeTestUtil.getMockedEmployees().values().stream().toList();
        when(dataService.getAllEmployees()).thenReturn(empList);

        List<EmployeeEntity> actual = service.getEmployeesByNameSearch("Jane Smith");

        assertEquals(1, actual.size());
        assertEquals("Jane Smith", actual.get(0).getName());
    }

    @Test
    void shouldReturnEmptyListWhenNoEmployeeMatchesName() {
        List<EmployeeEntity> empList = EmployeeTestUtil.getMockedEmployees().values().stream().toList();
        when(dataService.getAllEmployees()).thenReturn(empList);

        List<EmployeeEntity> actual = service.getEmployeesByNameSearch("XyZ");

        assertEquals(0, actual.size());
        verify(dataService).getAllEmployees();
    }

    @Test
    void shouldGetHighestSalaryOfEmployees() {
        List<EmployeeEntity> empList = EmployeeTestUtil.getMockedEmployees().values().stream().toList();
        when(dataService.getAllEmployees()).thenReturn(empList);

        Integer highestSalaryOfEmployees = service.getHighestSalaryOfEmployees();

        assertEquals(60000, highestSalaryOfEmployees);
        verify(dataService).getAllEmployees();
    }

    @Test
    void shouldThrowExceptionOnGetHighestSalaryOfEmployees() {
        when(dataService.getAllEmployees()).thenReturn(List.of());

        EmployeeChallengeException error = assertThrows(EmployeeChallengeException.class, () -> service.getHighestSalaryOfEmployees());

        assertEquals("No Employees Found", error.getMessage());
        verify(dataService).getAllEmployees();
    }

    @Test
    void shouldGetTopTenHighestEarningEmployeeNames() {
        List<EmployeeEntity> empList = new ArrayList<>(EmployeeTestUtil.getMockedEmployees().values().stream().toList());
        empList.addAll(EmployeeTestUtil.getMockedEmployees().values().stream().toList());
        empList.addAll(EmployeeTestUtil.getMockedEmployees().values().stream().toList());
        empList.addAll(EmployeeTestUtil.getMockedEmployees().values().stream().toList());
        when(dataService.getAllEmployees()).thenReturn(empList);

        List<String> actual = service.getTopTenHighestEarningEmployeeNames();
        List<String> expected = List.of("Jane Smith", "Jane Smith", "Jane Smith", "Jane Smith", "John Doe", "John Doe", "John Doe", "John Doe", "Bob Johnson", "Bob Johnson");
        assertEquals(10, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTopTenHighestEarningEmployeeNamesForLessThan10Employees() {
        List<EmployeeEntity> empList = EmployeeTestUtil.getMockedEmployees().values().stream().toList();
        when(dataService.getAllEmployees()).thenReturn(empList);

        List<String> actual = service.getTopTenHighestEarningEmployeeNames();
        List<String> expected = List.of("Jane Smith", "John Doe", "Bob Johnson");
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
    }
}