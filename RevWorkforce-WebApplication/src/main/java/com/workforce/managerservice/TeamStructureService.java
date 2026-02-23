package com.workforce.managerservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workforce.model.Employee;
import com.workforce.repository.EmployeeRepository;

@Service
public class TeamStructureService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Employee> getTeamMembers(Long managerId) {
		return employeeRepository.findByManager_Id(managerId);
	}
}
