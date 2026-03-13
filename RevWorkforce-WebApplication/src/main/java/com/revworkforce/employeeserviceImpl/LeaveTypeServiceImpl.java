package com.revworkforce.employeeserviceImpl;

import com.revworkforce.model.LeaveType;
import com.revworkforce.repository.LeaveTypeRepository;
import com.revworkforce.employeeservice.LeaveTypeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

	private static final Logger logger = LogManager.getLogger(LeaveTypeServiceImpl.class);

	private final LeaveTypeRepository leaveTypeRepository;

	public LeaveTypeServiceImpl(LeaveTypeRepository leaveTypeRepository) {
		this.leaveTypeRepository = leaveTypeRepository;
	}

	@Override
	public List<LeaveType> getAllLeaveTypes() {

		logger.info("Fetching all leave types");

		List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

		logger.debug("Total leave types fetched {}", leaveTypes.size());

		return leaveTypes;
	}

}