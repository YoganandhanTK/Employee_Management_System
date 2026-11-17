package com.empmanagement.Service;

import com.empmanagement.Entity.Employee;
import com.empmanagement.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public Employee saveEmployee(Employee employee) {
		if (employeeRepository.existsByEmail(employee.getEmail())) {
			throw new RuntimeException("Error: An employee with this email already exists!");
		}

		return employeeRepository.save(employee);
	}

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id).orElse(null);
	}

	public Optional<Employee> getEmployeeByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}

	public Employee updateEmployee(Long id, Employee updatedEmployeeData) {

		Employee existingEmployee = employeeRepository.findById(id).orElse(null);

		if (existingEmployee == null) {
			return null;
		}

		if (!existingEmployee.getEmail().equals(updatedEmployeeData.getEmail())
				&& employeeRepository.existsByEmail(updatedEmployeeData.getEmail())) {
			throw new RuntimeException("Error: An employee with this email already exists!");
		}

		existingEmployee.setName(updatedEmployeeData.getName());
		existingEmployee.setEmail(updatedEmployeeData.getEmail());
		existingEmployee.setDepartment(updatedEmployeeData.getDepartment());
		existingEmployee.setSalary(updatedEmployeeData.getSalary());
		existingEmployee.setHireDate(updatedEmployeeData.getHireDate());

		return employeeRepository.save(existingEmployee);
	}

	public Page<Employee> getAllEmployeesPaginated(int page, int size, String sortBy, String direction) {

		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		return employeeRepository.findAll(pageable);
	}
}
