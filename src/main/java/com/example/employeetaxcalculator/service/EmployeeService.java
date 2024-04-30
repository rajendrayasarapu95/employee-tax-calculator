package com.example.employeetaxcalculator.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.employeetaxcalculator.model.Employee;

@Service
public class EmployeeService {

	private final Map<Integer, Employee> employeeDetails;

	public EmployeeService(Map<Integer, Employee> employeeDetails) {
		this.employeeDetails = employeeDetails;
	}
	
	public Employee getEmployee(int employeeId) {
		if(employeeDetails.containsKey(employeeId)) {
			return employeeDetails.get(employeeId);
		}
		return null;
	}
	
	public void saveEmployee(Employee emp) {
		employeeDetails.put(emp.getEmployeeId(), emp);
	}
}
