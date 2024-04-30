package com.example.employeetaxcalculator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.employeetaxcalculator.model.Employee;

@Configuration
public class ApplicationDataConfiguration {

	@Bean
	public Map<Integer, Employee> employeeDetails() {
		final Map<Integer, Employee> employeeDetails = new HashMap<>();
		List<Long> phoneNumbers = new ArrayList<>();
		phoneNumbers.add(9874563210L);
		Employee emp = new Employee(1, "John", "Doe", "johnDoe@mail.com", phoneNumbers, "01/01/2020", 62500);
		employeeDetails.put(1, emp);
		return employeeDetails;
	}
}
