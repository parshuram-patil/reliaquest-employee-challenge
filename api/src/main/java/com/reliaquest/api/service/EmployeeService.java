package com.reliaquest.api.service;

import com.reliaquest.api.dto.CreateEmployeeRequestDto;
import com.reliaquest.api.dto.DeleteEmployeeRequestDto;
import com.reliaquest.api.dto.EmployeeEntity;
import com.reliaquest.api.exception.EmployeeChallengeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeDataService dataService;

    public List<EmployeeEntity> getAllEmployees() {
        return dataService.getAllEmployees();
    }

    public EmployeeEntity getEmployee(UUID id) {
        return dataService.getEmployee(id);
    }

    public EmployeeEntity createEmployee(CreateEmployeeRequestDto employeeRequest) {
        return dataService.createEmployee(employeeRequest);
    }

    public Boolean deleteEmployee(String name) {
        return dataService.deleteEmployee(new DeleteEmployeeRequestDto(name));
    }

    public List<EmployeeEntity> getEmployeesByNameSearch(String searchString) {
        return getAllEmployees().stream()
                .filter(emp -> emp.employeeName().equalsIgnoreCase(searchString))
                .toList();
    }

    public Integer getHighestSalaryOfEmployees() {
        Optional<EmployeeEntity> emp = getAllEmployees().stream()
                .max(Comparator.comparingDouble(EmployeeEntity::employeeSalary));

        if(emp.isEmpty()) {
            throw new EmployeeChallengeException("No Employees Found");
        }

        return emp.get().employeeSalary();
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        return getAllEmployees().stream()
                .sorted((e1, e2) -> Double.compare(e2.employeeSalary(), e1.employeeSalary()))
                .limit(10)
                .map(EmployeeEntity::employeeName)
                .toList();
    }
}
