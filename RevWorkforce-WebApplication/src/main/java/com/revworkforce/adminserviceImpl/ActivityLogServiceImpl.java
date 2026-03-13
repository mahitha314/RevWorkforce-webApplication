package com.revworkforce.adminserviceImpl;

import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.model.SystemActivityLog;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.SystemActivityLogRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

	private static final Logger logger = LogManager.getLogger(ActivityLogServiceImpl.class);

	private final SystemActivityLogRepository repository;
	private final EmployeeRepository employeeRepository;

	public ActivityLogServiceImpl(SystemActivityLogRepository repository, EmployeeRepository employeeRepository) {

		this.repository = repository;
		this.employeeRepository = employeeRepository;
	}

	@Override
	public void log(String action, String module, String description, String status, HttpServletRequest request) {

		logger.info("Creating activity log. Action: {}, Module: {}", action, module);

		try {

			String username = SecurityContextHolder.getContext().getAuthentication().getName();

			logger.debug("Authenticated user: {}", username);

			Employee emp = employeeRepository.findByEmail(username).orElseThrow(() -> {
				logger.error("Employee not found for email {}", username);
				return new RuntimeException("Employee not found");
			});

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

			logger.info("Activity log saved successfully for user {}", emp.getEmail());

		} catch (Exception e) {

			logger.error("Error occurred while saving activity log", e);
		}
	}

	@Override
	public void log(String action, String module, String description, String status, LeaveRequest request) {

		logger.info("Creating activity log for leave request. Action: {}, Module: {}", action, module);

		try {

			SystemActivityLog log = new SystemActivityLog();

			log.setAction(action);
			log.setModule(module);
			log.setDescription(description);
			log.setStatus(status);
			log.setCreatedAt(LocalDateTime.now());

			repository.save(log);

			logger.info("Leave related activity log saved successfully");

		} catch (Exception e) {

			logger.error("Error occurred while saving leave activity log", e);
		}
	}

	@Override
	public void log(String action, String module) {

		logger.info("Activity log triggered. Action: {}, Module: {}", action, module);

		try {

			SystemActivityLog log = new SystemActivityLog();

			log.setAction(action);
			log.setModule(module);
			log.setCreatedAt(LocalDateTime.now());

			repository.save(log);

			logger.debug("Basic activity log saved");

		} catch (Exception e) {

			logger.error("Error while saving basic activity log", e);
		}
	}

	@Override
	public void log(Long id, String module) {

		logger.info("Logging activity for userId: {} in module: {}", id, module);

		try {

			SystemActivityLog log = new SystemActivityLog();

			log.setUserId(id);
			log.setModule(module);
			log.setCreatedAt(LocalDateTime.now());

			repository.save(log);

			logger.debug("User activity log stored successfully");

		} catch (Exception e) {

			logger.error("Error saving user activity log", e);
		}
	}

}