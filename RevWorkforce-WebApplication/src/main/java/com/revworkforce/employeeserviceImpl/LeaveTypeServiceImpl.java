package com.revworkforce.employeeserviceImpl;

import com.revworkforce.model.LeaveType;
import com.revworkforce.repository.LeaveTypeRepository;
import com.revworkforce.employeeservice.LeaveTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeServiceImpl(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    @Override
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }
}