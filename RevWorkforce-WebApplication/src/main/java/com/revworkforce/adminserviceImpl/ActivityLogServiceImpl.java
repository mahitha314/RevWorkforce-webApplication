package com.revworkforce.adminserviceImpl;

import java.time.LocalDateTime;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.model.SystemActivityLog;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.SystemActivityLogRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {
	private SystemActivityLogRepository repository;
	private EmployeeRepository employeeRepository;

	public ActivityLogServiceImpl(SystemActivityLogRepository repository, EmployeeRepository employeeRepository) {
		this.repository = repository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public void log(String action, String module, String description, String status, HttpServletRequest request) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Employee emp = employeeRepository.findByEmail(username)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		SystemActivityLog log = new SystemActivityLog();
		log.setUserId(emp.getId());
		log.setUserName(emp.getFirstName());
		log.setRole(emp.getRole());
		log.setAction(action);
		log.setModule(module);
		log.setDescription(description);
		log.setStatus(status);
		log.setIpAddress(request.getRemoteAddr());
		log.setCreatedAt(LocalDateTime.now());

		repository.save(log);

	}

	@Override
	public void log(String action, String module, String description, String status, LeaveRequest request) {
	}

	@Override
	public void log(String action, String module) {
	}

	@Override
	public void log(Long id, String module) {
	}
}