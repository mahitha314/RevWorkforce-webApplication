package com.revworkforce.managerserviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.managerservice.TeamLeavesService;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveApproval;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.model.Notification;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.LeaveApprovalRepository;
import com.revworkforce.repository.LeaveBalanceRepository;
import com.revworkforce.repository.LeaveRequestRepository;
import com.revworkforce.repository.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
public class TeamLeavesServiceImpl implements TeamLeavesService {

    private final EmployeeRepository employeeRepo;
    private final LeaveRequestRepository leaveRequestRepo;
    private final LeaveApprovalRepository approvalRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;
    private final NotificationRepository notificationRepo;

    public TeamLeavesServiceImpl(EmployeeRepository employeeRepo,
                                 LeaveRequestRepository leaveRequestRepo,
                                 LeaveApprovalRepository approvalRepo,
                                 LeaveBalanceRepository leaveBalanceRepo,
                                 NotificationRepository notificationRepo) {
        this.employeeRepo = employeeRepo;
        this.leaveRequestRepo = leaveRequestRepo;
        this.approvalRepo = approvalRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.notificationRepo = notificationRepo;
    }

    @Override
    public ApiResponse getDirectReportees(Long managerId) {

        if (!employeeRepo.existsById(managerId)) {
            return new ApiResponse(404, "Manager not found", null);
        }

        List<Employee> team = employeeRepo.findByManager_Id(managerId);

        return new ApiResponse(200, "Direct reportees fetched", team);
    }

    @Override
    public ApiResponse getTeamLeaveRequests(Long managerId) {

        if (!employeeRepo.existsById(managerId)) {
            return new ApiResponse(404, "Manager not found", null);
        }

        List<LeaveRequest> requests =
                leaveRequestRepo.findByEmployee_Manager_Id(managerId);

        return new ApiResponse(200, "Team leave requests fetched", requests);
    }

    @Override
    @Transactional
    public ApiResponse approveLeave(Long managerId,
                                    Long leaveId,
                                    String comments) {

        LeaveRequest request = leaveRequestRepo.findById(leaveId).orElse(null);

        if (request == null) {
            return new ApiResponse(404, "Leave not found", null);
        }
        
        if (request.getLeaveApproval() != null) {
            return new ApiResponse(400,
                    "Leave already processed",
                    null);
        }

        if (request.getEmployee().getManager() == null ||
            !request.getEmployee().getManager().getId().equals(managerId)) {

            return new ApiResponse(403,
                    "Unauthorized to approve this leave", null);
        }

        Employee manager = employeeRepo.findById(managerId).orElse(null);
        if (manager == null) {
            return new ApiResponse(404, "Manager not found", null);
        }

        LeaveApproval approval = new LeaveApproval();
        approval.setManager(manager);
        approval.setStatus("APPROVED");
        approval.setComments(comments);
        approval.setApprovalDate(LocalDate.now());

        approvalRepo.save(approval);

        request.setLeaveApproval(approval);
        leaveRequestRepo.save(request);

        Notification notification = new Notification();
        notification.setEmployee(request.getEmployee());
        notification.setTitle("Leave Approved");
        notification.setMessage("Your leave has been approved");
        notification.setStatus("ACTIVE"); 
        notification.setType("LEAVE");           
        notification.setIsRead(false);             
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepo.save(notification);

        return new ApiResponse(200, "Leave approved successfully", request);
    }

    @Override
    @Transactional
    public ApiResponse rejectLeave(Long managerId,
                                   Long leaveId,
                                   String comments) {

        if (comments == null || comments.isBlank()) {
            return new ApiResponse(400,
                    "Comments mandatory for rejection", null);
        }

        LeaveRequest request = leaveRequestRepo.findById(leaveId)
                .orElse(null);

        if (request == null) {
            return new ApiResponse(404, "Leave not found", null);
        }
        
        if (request.getLeaveApproval() != null) {
            return new ApiResponse(400,
                    "Leave already processed",
                    null);
        }

        if (request.getEmployee().getManager() == null ||
            !request.getEmployee().getManager().getId().equals(managerId)) {

            return new ApiResponse(403,
                    "Unauthorized to reject this leave", null);
        }

        Employee manager = employeeRepo.findById(managerId).orElse(null);

        LeaveApproval approval = new LeaveApproval();
        approval.setManager(manager);
        approval.setStatus("REJECTED");
        approval.setComments(comments);
        approval.setApprovalDate(LocalDate.now());

        approvalRepo.save(approval);

        request.setLeaveApproval(approval);
        leaveRequestRepo.save(request);

        Notification notification = new Notification();
        notification.setEmployee(request.getEmployee());
        notification.setTitle("Leave Rejected");
        notification.setMessage("Your leave was rejected. Reason: " + comments);
        notification.setStatus("ACTIVE");
        notification.setType("LEAVE");
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepo.save(notification);

        return new ApiResponse(200,
                "Leave rejected successfully",
                request);
    }

    @Override
    public ApiResponse getTeamLeaveCalendar(Long managerId) {

        if (!employeeRepo.existsById(managerId)) {
            return new ApiResponse(404, "Manager not found", null);
        }

        List<LeaveRequest> calendar =
                leaveRequestRepo.findByEmployee_Manager_Id(managerId);

        return new ApiResponse(200, "Team leave calendar fetched", calendar);
    }

    @Override
    public ApiResponse getTeamLeaveBalance(Long managerId) {

        if (!employeeRepo.existsById(managerId)) {
            return new ApiResponse(404, "Manager not found", null);
        }

        List<Employee> team = employeeRepo.findByManager_Id(managerId);

        Map<String, Object> result = new HashMap<>();

        for (Employee emp : team) {
            result.put(
                emp.getFirstName() + " " + emp.getLastName(),
                leaveBalanceRepo.findByEmployeeId(emp.getId())
            );
        }

        return new ApiResponse(200,
                "Team leave balance fetched",
                result);
    }
    
}