package com.example.employeetaxcalculator.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeetaxcalculator.model.Employee;
import com.example.employeetaxcalculator.service.EmployeeService;

@RestController
@RequestMapping("employeeTax")
public class EmployeeTaxController {

	private final EmployeeService employeeService;
	
	public EmployeeTaxController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@GetMapping("/{employeeId}")
	public ResponseEntity getEmployeeTaxDeductions(@PathVariable int employeeId) {
		Employee emp = employeeService.getEmployee(employeeId);		
		if(emp != null) {
			String dateOfJoining = emp.getDateOfJoining();
			double salary = emp.getSalary();
						
			double tax = 0;
			
			LocalDate doj = LocalDate.parse(dateOfJoining, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        LocalDate currentDate = LocalDate.now();
	        int currentYear = currentDate.getYear();
	        int currentMonth = currentDate.getMonthValue();

	        // Adjust salary based on DOJ
	        if (doj.getYear() == currentYear && doj.getMonthValue() == currentMonth) {
	            int daysWorkedThisMonth = (int) ChronoUnit.DAYS.between(doj, currentDate) + 1;
	            double dailySalary = salary / currentDate.lengthOfMonth();
	            salary -= dailySalary * (currentDate.lengthOfMonth() - daysWorkedThisMonth);
	        }

	        if (salary > 2500000) {
	            double cessAmount = (salary - 2500000) * 0.02;
	            tax += cessAmount;
	            salary -= cessAmount;
	        }

	        if (salary > 1000000) {
	            tax += (salary - 1000000) * 0.2;
	            salary = 1000000;
	        }
	        if (salary > 500000) {
	            tax += (salary - 500000) * 0.1;
	            salary = 500000;
	        }
	        if (salary > 250000) {
	            tax += (salary - 250000) * 0.05;
	        }
	        return ResponseEntity.ok(tax);
		}
		return null;
	}
}
