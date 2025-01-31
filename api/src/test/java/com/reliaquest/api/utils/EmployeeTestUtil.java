package com.reliaquest.api.utils;

import com.reliaquest.api.dto.EmployeeEntity;

import java.util.Map;
import java.util.UUID;

public class EmployeeTestUtil {
    private EmployeeTestUtil(){}

    public static Map<UUID, EmployeeEntity> getMockedEmployees() {
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
