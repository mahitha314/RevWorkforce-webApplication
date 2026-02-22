package com.workforce.adminservice;

import com.workforce.model.*;
import com.workforce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LeaveManagementService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final HolidayRepository holidayRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    public LeaveManagementService(LeaveRequestRepository leaveRequestRepository,
                                  HolidayRepository holidayRepository,
                                  LeaveBalanceRepository leaveBalanceRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.holidayRepository = holidayRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    // ================= LEAVE REQUESTS =================

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest approveLeave(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id).orElseThrow();
        leave.setStatus("APPROVED");
        return leaveRequestRepository.save(leave);
    }

    public LeaveRequest rejectLeave(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id).orElseThrow();
        leave.setStatus("REJECTED");
        return leaveRequestRepository.save(leave);
    }

    public long getPendingCount() {
        return leaveRequestRepository.countByStatus("PENDING");
    }

    public long getApprovedCount() {
        return leaveRequestRepository.countByStatus("APPROVED");
    }

    public long getRejectedCount() {
        return leaveRequestRepository.countByStatus("REJECTED");
    }

    // ================= HOLIDAYS =================

    public Holiday addHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);
    }

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }

    // ================= LEAVE BALANCE =================

    public List<LeaveBalance> getEmployeeLeaveBalance(Employee employee) {
        return leaveBalanceRepository.findByEmployee(employee);
    }

    // ================= LEAVE HISTORY =================

    public List<LeaveRequest> getLeaveHistory(Employee employee) {
        return leaveRequestRepository.findByEmployee(employee);
    }
}