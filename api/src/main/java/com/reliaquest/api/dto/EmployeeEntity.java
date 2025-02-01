package com.reliaquest.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class EmployeeEntity {
    UUID id;
    String name;
    Integer salary;
    Integer age;
    String title;
    String email;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("employee_name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("salary")
    public Integer getSalary() {
        return salary;
    }

    @JsonProperty("employee_salary")
    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @JsonProperty("age")
    public Integer getAge() {
        return age;
    }

    @JsonProperty("employee_age")
    public void setAge(Integer age) {
        this.age = age;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("employee_title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("employee_email")
    public void setEmail(String email) {
        this.email = email;
    }
}
