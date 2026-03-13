package com.revworkforce.employeeserviceImpl;

import com.revworkforce.model.LeaveType;
import com.revworkforce.repository.LeaveTypeRepository;
import com.revworkforce.employeeservice.LeaveTypeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

    private static final Logger logger =
            LoggerFactory.getLogger(LeaveTypeServiceImpl.class);

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeServiceImpl(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
        logger.info("LeaveTypeServiceImpl initialized");
    }

    @Override
    public List<LeaveType> getAllLeaveTypes() {

        logger.info("Fetching all leave types");

        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        logger.debug("Total leave types fetched: {}", leaveTypes.size());

        return leaveTypes;
    }
}