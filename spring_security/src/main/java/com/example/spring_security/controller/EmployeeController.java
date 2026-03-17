package com.example.spring_security.controller;

import com.example.spring_security.entity.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private List<Employee> employees = List.of(
            new Employee(1L, "John Doe", 50000.0),
            new Employee(2L, "Jane Smith", 60000.0),
            new Employee(3L, "Bob Johnson", 55000.0)
    );

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employees;
    }
}
