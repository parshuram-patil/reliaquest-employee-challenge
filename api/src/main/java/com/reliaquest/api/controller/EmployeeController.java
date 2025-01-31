package com.reliaquest.api.controller;

import com.reliaquest.api.dto.CreateEmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController<EmployeeEntity, CreateEmployeeRequestDto>{

    private final EmployeeService service;

    @Override
    @GetMapping
    public ResponseEntity<List<EmployeeEntity>> getAllEmployees() {
        return null;
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<EmployeeEntity>> getEmployeesByNameSearch(String searchString) {
        return null;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeEntity> getEmployeeById(UUID id) {
        return null;
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return null;
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return null;
    }

    @Override
    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(CreateEmployeeRequestDto employeeInput) {
        return null;
    }

    @Override
    @DeleteMapping("/{name}")
    public ResponseEntity<Boolean> deleteEmployeeByName(String name) {
        return null;
    }
}
