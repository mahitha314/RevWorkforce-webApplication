package com.revworkforce.employeeserviceImpl;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.DashboardDTO;
import com.revworkforce.model.*;
import com.revworkforce.repository.*;
import com.revworkforce.employeeservice.DashboardService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final GoalRepository goalRepository;
    private final LeaveBalanceRepository leaveBalanceRepo;
    private final LeaveRequestRepository leaveRequestRepo;
    private final PerformanceReviewRepository performanceReviewRepo;

    public DashboardServiceImpl(
            GoalRepository goalRepository,
            LeaveBalanceRepository leaveBalanceRepo,
            LeaveRequestRepository leaveRequestRepo,
            PerformanceReviewRepository performanceReviewRepo) {

        this.goalRepository = goalRepository;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.leaveRequestRepo = leaveRequestRepo;
        this.performanceReviewRepo = performanceReviewRepo;
    }

    @Override
    public ApiResponse getEmployeeDashboard(Long employeeId) {

        List<Goal> goals = goalRepository.findByEmployee_Id(employeeId);

        double goalPercentage =
                goals.stream()
                .mapToDouble(Goal::getProgress)
                .average()
                .orElse(0);

        List<LeaveBalance> balances =
                leaveBalanceRepo.findByEmployee_Id(employeeId);

        int totalAllocated =
                balances.stream().mapToInt(LeaveBalance::getTotalLeaves).sum();

        int leavesUsed =
                balances.stream().mapToInt(LeaveBalance::getUsedLeaves).sum();

        int remainingLeaves =
                balances.stream().mapToInt(LeaveBalance::getRemainingLeaves).sum();

        List<LeaveRequest> requests =
                leaveRequestRepo.findByEmployee_Id(employeeId);

        long pendingRequests =
                requests.stream()
                .filter(r -> r.getLeaveApproval() == null
                || (r.getLeaveApproval() != null
                && "Pending".equalsIgnoreCase(r.getLeaveApproval().getStatus())))
                .count();

        List<PerformanceReview> reviews =
                performanceReviewRepo.findByEmployee_Id(employeeId);

        double performanceRating =
                reviews.stream()
                .filter(r -> r.getManagerRating() > 0)
                .mapToDouble(PerformanceReview::getManagerRating)
                .average()
                .orElse(0);

        DashboardDTO dto = new DashboardDTO(
                Math.round(goalPercentage),
                totalAllocated,
                leavesUsed,
                remainingLeaves,
                pendingRequests,
                performanceRating
        );

        return new ApiResponse(
                200,
                "Dashboard data fetched successfully",
                dto
        );
    }
}