package com.example.employeetaxcalculator.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;
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
	public ResponseEntity getEmployeeDetailsWithTaxDeductions(@PathVariable int employeeId) {
		Employee emp = employeeService.getEmployee(employeeId);
		if (emp != null) {
			int workingDays = calculateWorkingDays(emp.getDateOfJoining());
			double salaryForDay = Math.round(emp.getSalary() / 30);
			double yearlySalary = salaryForDay * workingDays;
			double[] taxAndCessAmounts = calculateTaxAndCess(yearlySalary);

			JSONObject empWithTaxDetails = new JSONObject();
			empWithTaxDetails.put("Id", emp.getEmployeeId());
			empWithTaxDetails.put("First Name", emp.getFirstName());
			empWithTaxDetails.put("Last Name", emp.getLastName());
			empWithTaxDetails.put("Yearly Salary", yearlySalary);
			empWithTaxDetails.put("Tax Amount", taxAndCessAmounts[0]);
			empWithTaxDetails.put("Cess Amount", taxAndCessAmounts[1]);

			return ResponseEntity.ok(empWithTaxDetails.toString());
		}
		return ResponseEntity.notFound().build();
	}

	private int calculateWorkingDays(String dateOfJoining) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate doj = LocalDate.parse(dateOfJoining, formatter);

		int currentYear = LocalDate.now().getYear();
		LocalDate financialYearStartDate = LocalDate.parse("01/04/" + currentYear, formatter);
		LocalDate financialYearEndDate = LocalDate.parse("31/03/" + (currentYear + 1), formatter);

		if (doj.isAfter(financialYearStartDate)) {
			int workingDays = (int) ChronoUnit.DAYS.between(doj, financialYearEndDate);
			return workingDays;
		}

		// Case of Leap year not considered
		return 365;
	}

	private double[] calculateTaxAndCess(double annualSalary) {
		double tax = 0;
		double cessAmount = 0;

		if (annualSalary > 2500000) {
			double taxableIncomeForCess = annualSalary - 2500000;
			cessAmount = taxableIncomeForCess * 0.02;
			tax += cessAmount;
			annualSalary -= taxableIncomeForCess;
		}
		if (annualSalary > 1000000) {
			tax += (annualSalary - 1000000) * 0.2;
			annualSalary = 1000000;
		}
		if (annualSalary > 500000) {
			tax += (annualSalary - 500000) * 0.1;
			annualSalary = 500000;
		}
		if (annualSalary > 250000) {
			tax += (annualSalary - 250000) * 0.05;
		}

		return new double[] { tax, cessAmount };
	}
}
