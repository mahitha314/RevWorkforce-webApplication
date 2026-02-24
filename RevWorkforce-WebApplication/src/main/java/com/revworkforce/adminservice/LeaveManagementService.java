package com.workforce.adminservice;

import com.workforce.model.*;
import com.workforce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LeaveManagementService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final HolidayRepository holidayRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;
    private final PerformanceReviewRepository performanceReviewRepository;
    private final GoalRepository goalRepository; 
    //private final AnnouncementRepository announcementRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    public LeaveManagementService(LeaveRequestRepository leaveRequestRepository,
                                  HolidayRepository holidayRepository,
                                  LeaveBalanceRepository leaveBalanceRepository,
                                  EmployeeRepository employeeRepository,
                                  PerformanceReviewRepository performanceReviewRepository,
                                  GoalRepository goalRepository,
                                  //AnnouncementRepository announcementRepository,
                                  DepartmentRepository departmentRepository,
                                  DesignationRepository designationRepository) { // ✅ ADDED

        this.leaveRequestRepository = leaveRequestRepository;
        this.holidayRepository = holidayRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeRepository = employeeRepository;
        this.performanceReviewRepository = performanceReviewRepository;
        this.goalRepository = goalRepository; 
        //this.announcementRepository = announcementRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
    }

    
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest approveLeave(Long id) {

        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leave.setStatus("APPROVED");
        return leaveRequestRepository.save(leave);
    }

    public LeaveRequest rejectLeave(Long id) {

        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

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

    
    public Holiday addHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);
    }

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }


    public List<LeaveBalance> getEmployeeLeaveBalance(Employee employee) {
        return leaveBalanceRepository.findByEmployee(employee);
    }

    public List<LeaveBalance> getAllBalances() {
        return leaveBalanceRepository.findAll();
    }

    public List<LeaveBalance> getAllLeaveBalances() {
        return leaveBalanceRepository.findAll();
    }

  
    public List<LeaveRequest> getLeaveHistory(Employee employee) {
        return leaveRequestRepository.findByEmployee(employee);
    }

    

    public void assignLeaveQuota(Long empId,
                                 String leaveType,
                                 int totalDays) {

        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(employee);
        balance.setLeaveType(leaveType);
        balance.setTotalLeaves(totalDays);
        balance.setUsedLeaves(0);
        balance.setRemainingLeaves(totalDays);

        leaveBalanceRepository.save(balance);
    }


    public List<PerformanceReview> getAllPerformanceReviews() {
        return performanceReviewRepository.findAll();
    }

    public void reviewPerformance(Long reviewId,
                                  int managerRating,
                                  String managerFeedback) {

        PerformanceReview review = performanceReviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setManagerRating(managerRating);
        review.setManagerFeedback(managerFeedback);
        review.setStatus("Reviewed");

        performanceReviewRepository.save(review);
    }


    public List<Goal> getAllTeamGoals() {
        return goalRepository.findAll();
    }

    public Goal getGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
    }


public List<Department> getAllDepartments() {
  return departmentRepository.findAll();
}

public void addDepartment(Department department) {
  departmentRepository.save(department);
}

public Department getDepartmentById(Long id) {
  return departmentRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Department not found"));
}

public void updateDepartment(Department department) {
  departmentRepository.save(department);
}

public void deleteDepartment(Long id) {
  departmentRepository.deleteById(id);
}

public long getEmployeeCountByDepartment(Department department) {
  return employeeRepository.countByDepartment(department);
}

public List<Designation> getAllDesignations() {
  return designationRepository.findAll();
}

public void addDesignation(Designation designation) {
  designationRepository.save(designation);
}

public Designation getDesignationById(Long id) {
    return designationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not Found"));
}

public Designation saveDesignation(Designation designation) {
    return designationRepository.save(designation);
}

public void deleteDesignation(Long id) {
  designationRepository.deleteById(id);
}
public long getDesignationCount() {
    return designationRepository.count();
}
public List<String> getSystemActivityLogs() {

    List<String> logs = new ArrayList<>();

    for (LeaveRequest leave : leaveRequestRepository.findAll()) {
        logs.add(leave.getEmployee().getFirstName() +
                " applied leave (" + leave.getStatus() + ")");
    }

    for (PerformanceReview review : performanceReviewRepository.findAll()) {
        logs.add(review.getEmployee().getFirstName() +
                " submitted performance review");
    }

    return logs;
}
public List<String> getAdminNotifications() {

    List<String> notes = new ArrayList<>();

    for (LeaveRequest leave : leaveRequestRepository.findAll()) {
        if ("PENDING".equals(leave.getStatus())) {
            notes.add(leave.getEmployee().getFirstName()
                    + " applied for leave");
        }
    }

    for (PerformanceReview review :
            performanceReviewRepository.findAll()) {

        if ("Submitted".equals(review.getStatus())) {
            notes.add(review.getEmployee().getFirstName()
                    + " submitted performance review");
        }
    }

    return notes;
}
}