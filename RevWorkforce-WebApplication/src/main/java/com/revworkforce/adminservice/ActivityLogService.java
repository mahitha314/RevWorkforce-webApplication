package com.revworkforce.adminservice;

import com.revworkforce.model.LeaveRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface ActivityLogService {
	 void log(String action,
             String module,
             String description,
             String status,
             HttpServletRequest request);

	 void log(String action, String module, String description, String status, LeaveRequest request);

	 void log(String action, String module);

	 void log(Long id, String module);
}
