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
        return ResponseEntity.ok().body(service.getAllEmployees());
    }

    @Override
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<EmployeeEntity>> getEmployeesByNameSearch(String searchString) {
        return ResponseEntity.ok().body(service.getEmployeesByNameSearch(searchString));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeEntity> getEmployeeById(UUID id) {
        return ResponseEntity.ok().body(service.getEmployee(id));
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok().body(service.getHighestSalaryOfEmployees());
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok().body(service.getTopTenHighestEarningEmployeeNames());
    }

    @Override
    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(CreateEmployeeRequestDto employeeInput) {
        return ResponseEntity.ok().body(service.createEmployee(employeeInput));
    }

    @Override
    @DeleteMapping("/{name}")
    public ResponseEntity<Boolean> deleteEmployeeByName(String name) {
        return ResponseEntity.ok().body(service.deleteEmployee(name));
    }
}
