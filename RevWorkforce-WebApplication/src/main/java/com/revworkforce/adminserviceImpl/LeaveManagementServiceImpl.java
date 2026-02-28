package com.revworkforce.adminserviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revworkforce.adminservice.LeaveManagementService;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveType;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveBalanceRepository;
import com.revworkforce.repository.LeaveTypeRepository;

@Service
public class LeaveManagementServiceImpl implements LeaveManagementService {

    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;

    // ✅ Constructor Injection (BEST PRACTICE)
    public LeaveManagementServiceImpl(
            LeaveTypeRepository leaveTypeRepository,
            LeaveBalanceRepository leaveBalanceRepository,
            EmployeeRepository employeeRepository) {

        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // ================= LEAVE TYPE =================

    @Override
    public LeaveTypeDTO createLeaveType(LeaveTypeDTO dto) {

        LeaveType leaveType = new LeaveType();
        leaveType.setTypeName(dto.getTypeName());
        leaveType.setTotalDays(dto.getTotalDays());

        LeaveType saved = leaveTypeRepository.save(leaveType);

        return new LeaveTypeDTO(
                saved.getId(),
                saved.getTypeName(),
                saved.getTotalDays()
        );
    }

    @Override
    public List<LeaveTypeDTO> getAllLeaveTypes() {

        return leaveTypeRepository.findAll()
                .stream()
                .map(type -> new LeaveTypeDTO(
                        type.getId(),
                        type.getTypeName(),
                        type.getTotalDays()
                ))
                .toList();
    }

    // ================= ASSIGN =================

    @Override
    public LeaveBalance assignLeaveToEmployee(Long employeeId,
                                              Long leaveTypeId,
                                              int totalDays) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId).orElseThrow();

        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(employee);
        balance.setLeaveType(leaveType);
        balance.setTotalLeaves(totalDays);
        balance.setUsedLeaves(0);
        balance.setRemainingLeaves(totalDays);

        return leaveBalanceRepository.save(balance);
    }

    // ================= ADJUST =================

    @Override
    public LeaveBalance adjustLeave(LeaveBalanceDTO dto) {

        Employee employee = employeeRepository.findById(dto.getEmployeeId()).orElseThrow();
        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId()).orElseThrow();

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeAndLeaveType(employee, leaveType)
                .orElseThrow();
        balance.setTotalLeaves(balance.getTotalLeaves() + dto.getDays());
        balance.setRemainingLeaves(balance.getRemainingLeaves() + dto.getDays());
        return leaveBalanceRepository.save(balance);
    }

    // ================= REPORTS =================

    @Override
    public List<LeaveBalance> getEmployeeLeaveInfo(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElseThrow();
        return leaveBalanceRepository.findByEmployee(employee);
    }

    @Override
    public List<LeaveBalance> getDepartmentLeaveReport(Long departmentId) {
        return leaveBalanceRepository.findByDepartmentId(departmentId);
    }
    @Override
    public long countLeaves() {
        return leaveTypeRepository.count();
    }
}