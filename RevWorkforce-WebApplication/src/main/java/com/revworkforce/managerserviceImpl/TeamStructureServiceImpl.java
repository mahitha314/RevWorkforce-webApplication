package com.revworkforce.managerserviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.TeamMemberDTO;
import com.revworkforce.exception.ResourceNotFoundException;
import com.revworkforce.managerservice.TeamStructureService;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Service
public class TeamStructureServiceImpl implements TeamStructureService {

	private final EmployeeRepository employeeRepo;

	public TeamStructureServiceImpl(EmployeeRepository employeeRepo) {
		this.employeeRepo = employeeRepo;
	}

	@Override
	public ApiResponse getTeamStructure(Long managerId) {

		Employee manager = employeeRepo.findById(managerId)
				.orElseThrow(() -> new ResourceNotFoundException("Manager not found"));

		if (!"MANAGER".equalsIgnoreCase(manager.getRole())) {
			throw new IllegalArgumentException("Only managers can view team structure");
		}

		List<Employee> team = employeeRepo.findByManager_Id(managerId);

		List<TeamMemberDTO> teamDTO = team.stream()
				.map(emp -> new TeamMemberDTO(emp.getId(), emp.getEmployeeId(),
						emp.getFirstName() + " " + emp.getLastName(), emp.getEmail(), emp.getRole(),
						emp.getDepartment().getName(), emp.getDesignation().getTitle()))
				.collect(Collectors.toList());

		return new ApiResponse(200, "Team structure fetched", teamDTO);
	}

	@Override
	public ApiResponse getTeamMemberProfile(Long managerId, Long employeeId) {

		Employee employee = employeeRepo.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		if (employee.getManager() == null || !employee.getManager().getId().equals(managerId)) {

			throw new IllegalArgumentException("You are not authorized to view this employee profile");
		}

		TeamMemberDTO dto = new TeamMemberDTO(employee.getId(), employee.getEmployeeId(),
				employee.getFirstName() + " " + employee.getLastName(), employee.getEmail(), employee.getRole(),
				employee.getDepartment().getName(), employee.getDesignation().getTitle());

		return new ApiResponse(200, "Employee profile fetched", dto);
	}

}