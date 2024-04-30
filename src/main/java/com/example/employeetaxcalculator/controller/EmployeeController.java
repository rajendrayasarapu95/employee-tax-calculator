package com.example.employeetaxcalculator.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeetaxcalculator.model.Employee;
import com.example.employeetaxcalculator.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@GetMapping("/{employeeId}")
	public ResponseEntity getEmployee(@PathVariable int employeeId) {
		Employee emp = employeeService.getEmployee(employeeId);
		if(emp == null) {
			ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(emp);
	}
	
	@PostMapping("/save")
	public ResponseEntity<String> saveEmployee(@RequestBody Employee employee) {
		List<String> errorList = validateEmployee(employee);
		if(errorList.size() == 0) {
			employeeService.saveEmployee(employee);
			return ResponseEntity.ok().build();
		}
		return new ResponseEntity<>(errorList.get(0), HttpStatus.BAD_REQUEST);	
	}
	
	private List<String> validateEmployee(Employee employee) {
		List<String> errorList = new ArrayList<>();
		if(employee.getFirstName().isBlank() || employee.getFirstName() == null) {
			errorList.add("Please enter a valid First Name");
		}
		if(employee.getLastName().isBlank() || employee.getLastName() == null) {
			errorList.add("Please enter a valid Last Name");
		}
		if(employee.getEmail().isBlank() || employee.getEmail() == null) {
			errorList.add("Please enter a valid email");
		}
		if(employee.getPhoneNumbers().isEmpty()) {
			errorList.add("Please enter at least one phone number");
		}
		if(employee.getSalary() <= 0 ) {
			errorList.add("Please enter salary amount more than 0");
		}
		return errorList;
	}
}
