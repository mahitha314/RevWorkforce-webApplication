package com.revworkforce.adminservice;

import jakarta.servlet.http.HttpServletRequest;

public interface ActivityLogService {
	 void log(String action,
             String module,
             String description,
             String status,
             HttpServletRequest request);
}
