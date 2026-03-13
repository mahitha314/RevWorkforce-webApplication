package com.revworkforce.managerserviceImpl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.TeamMemberDTO;
import com.revworkforce.exception.ResourceNotFoundException;
import com.revworkforce.managerservice.TeamStructureService;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;

@Service
public class TeamStructureServiceImpl implements TeamStructureService {

	private static final Logger logger = LogManager.getLogger(TeamStructureServiceImpl.class);

	private final EmployeeRepository employeeRepo;
	private final ActivityLogService activityLogService;

	public TeamStructureServiceImpl(EmployeeRepository employeeRepo, ActivityLogService activityLogService) {
		this.employeeRepo = employeeRepo;
		this.activityLogService = activityLogService;
	}

	private Employee validateManager(Long managerId) {

		logger.debug("Validating manager with id {}", managerId);

		Employee manager = employeeRepo.findById(managerId).orElseThrow(() -> {
			logger.error("Manager not found with id {}", managerId);
			return new ResourceNotFoundException("Manager not found");
		});

		if (!"MANAGER".equalsIgnoreCase(manager.getRole())) {

			logger.warn("User {} attempted to access manager functionality", managerId);
			throw new IllegalStateException("Only managers can access team structure");
		}

		return manager;
	}

	@Override
	public ApiResponse getTeamStructure(Long managerId) {

		logger.info("Fetching team structure for manager {}", managerId);

		Employee manager = employeeRepo.findById(managerId).orElseThrow(() -> {
			logger.error("Manager not found with id {}", managerId);
			return new ResourceNotFoundException("Manager not found");
		});

		if (!"MANAGER".equalsIgnoreCase(manager.getRole())) {

			logger.warn("Unauthorized access attempt to team structure by user {}", managerId);
			throw new IllegalArgumentException("Only managers can view team structure");
		}

		List<Employee> team = employeeRepo.findByManager_Id(managerId);

		logger.debug("Total team members found for manager {} : {}", managerId, team.size());

		List<TeamMemberDTO> teamDTO = team.stream()
				.map(emp -> new TeamMemberDTO(emp.getId(), emp.getEmployeeId(),
						emp.getFirstName() + " " + emp.getLastName(), emp.getEmail(), emp.getRole(),
						emp.getDepartment().getName(), emp.getDesignation().getTitle()))
				.collect(Collectors.toList());

		activityLogService.log(managerId, "Viewed team structure");

		logger.info("Team structure fetched successfully for manager {}", managerId);

		return new ApiResponse(200, "Team structure fetched", teamDTO);
	}

	@Override
	public ApiResponse getTeamMemberProfile(Long managerId, Long employeeId) {

		logger.info("Manager {} fetching profile for employee {}", managerId, employeeId);

		Employee employee = employeeRepo.findById(employeeId).orElseThrow(() -> {
			logger.error("Employee not found with id {}", employeeId);
			return new ResourceNotFoundException("Employee not found");
		});

		if (employee.getManager() == null || !employee.getManager().getId().equals(managerId)) {

			logger.warn("Unauthorized profile access attempt by manager {} for employee {}", managerId, employeeId);

			throw new IllegalArgumentException("You are not authorized to view this employee profile");
		}

		TeamMemberDTO dto = new TeamMemberDTO(employee.getId(), employee.getEmployeeId(),
				employee.getFirstName() + " " + employee.getLastName(), employee.getEmail(), employee.getRole(),
				employee.getDepartment().getName(), employee.getDesignation().getTitle());

		activityLogService.log(managerId, "Viewed profile of employee ID: " + employeeId);

		logger.info("Employee profile fetched successfully for employee {}", employeeId);

		return new ApiResponse(200, "Employee profile fetched", dto);
	}

}