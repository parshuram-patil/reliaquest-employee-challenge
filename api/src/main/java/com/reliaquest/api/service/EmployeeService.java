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
        log.info("Fetching all employees");
        return dataService.getAllEmployees();
    }

    public EmployeeEntity getEmployee(UUID id) {
        log.info("Fetching employee with id {}", id);
        return dataService.getEmployee(id);
    }

    public EmployeeEntity createEmployee(CreateEmployeeRequestDto employeeRequest) {
        log.info("Creating Employee with name {}", employeeRequest.name());
        return dataService.createEmployee(employeeRequest);
    }

    public Boolean deleteEmployee(String name) {
        log.info("Deleting employee with name {}", name);
        return dataService.deleteEmployee(new DeleteEmployeeRequestDto(name));
    }

    public List<EmployeeEntity> getEmployeesByNameSearch(String searchString) {
        log.info("Searching all employees with with pattern '{}'", searchString);
        return getAllEmployees().stream()
                .filter(emp -> emp.getName().equalsIgnoreCase(searchString))
                .toList();
    }

    public Integer getHighestSalaryOfEmployees() {
        log.info("Finding highest salary of employees");
        Optional<EmployeeEntity> emp = getAllEmployees().stream()
                .max(Comparator.comparingDouble(EmployeeEntity::getSalary));

        if(emp.isEmpty()) {
            String errMsg = "No Employees Found";
            log.error(errMsg);
            throw new EmployeeChallengeException(errMsg);
        }

        return emp.get().getSalary();
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        log.info("Fetching top 10 highest earning employee names");
        return getAllEmployees().stream()
                .sorted((e1, e2) -> Double.compare(e2.getSalary(), e1.getSalary()))
                .limit(10)
                .map(EmployeeEntity::getName)
                .toList();
    }
}
