package com.revworkforce.adminserviceImpl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ActivityLogServiceImpl.class);

    private SystemActivityLogRepository repository;
    private EmployeeRepository employeeRepository;

    public ActivityLogServiceImpl(SystemActivityLogRepository repository, EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void log(String action, String module, String description, String status, HttpServletRequest request) {

        logger.info("Activity log request received. Action: {}, Module: {}", action, module);

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            logger.debug("Fetching employee for username: {}", username);

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

            logger.info("Activity log saved successfully for user: {}", emp.getEmail());

        } catch (Exception e) {
            logger.error("Failed to save activity log for action: {} and module: {}", action, module, e);
        }
    }

    @Override
    public void log(String action, String module, String description, String status, LeaveRequest request) {
        logger.warn("LeaveRequest based logging not implemented yet. Action: {}, Module: {}", action, module);
    }

    @Override
    public void log(String action, String module) {
        logger.warn("Simple logging method called but not implemented. Action: {}, Module: {}", action, module);
    }

    @Override
    public void log(Long id, String module) {
        logger.warn("ID based logging method called but not implemented. ID: {}, Module: {}", id, module);
    }
}