package com.revworkforce.employeeserviceImpl;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.GoalDTO;
import com.revworkforce.employeeservice.GoalService;
import com.revworkforce.exception.EmployeeNotFoundException;
import com.revworkforce.exception.GoalUpdateException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Goal;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.GoalRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepo;
    private final EmployeeRepository employeeRepo;

    public GoalServiceImpl(GoalRepository goalRepo,
                           EmployeeRepository employeeRepo) {
        this.goalRepo = goalRepo;
        this.employeeRepo = employeeRepo;
    }

    // ================= CREATE GOAL =================
    @Override
    public ApiResponse createGoal(Long employeeId, GoalDTO dto) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        Goal goal = new Goal();
        goal.setEmployee(employee);
        goal.setGoalDescription(dto.getGoalDescription());
        goal.setPriority(dto.getPriority());
        goal.setStatus("NOT_STARTED");
        goal.setDeadline(dto.getDeadline());
        goal.setProgress(0);

        Goal savedGoal = goalRepo.save(goal);

        GoalDTO responseDTO = new GoalDTO(
                savedGoal.getId(),
                employee.getId(),
                employee.getFirstName() + " " + employee.getLastName(),
                savedGoal.getGoalDescription(),
                savedGoal.getPriority(),
                savedGoal.getStatus(),
                savedGoal.getDeadline(),
                savedGoal.getProgress()
        );

        return new ApiResponse(
                201,
                "Goal created successfully",
                responseDTO
        );
    }

    // ================= GET EMPLOYEE GOALS =================
    @Override
    public ApiResponse getEmployeeGoals(Long employeeId) {

        employeeRepo.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));

        List<GoalDTO> goals = goalRepo.findByEmployee_Id(employeeId)
                .stream()
                .map(goal -> new GoalDTO(
                        goal.getId(),
                        goal.getEmployee().getId(),
                        goal.getEmployee().getFirstName() + " " +
                                goal.getEmployee().getLastName(),
                        goal.getGoalDescription(),
                        goal.getPriority(),
                        goal.getStatus(),
                        goal.getDeadline(),
                        goal.getProgress()
                ))
                .collect(Collectors.toList());

        return new ApiResponse(
                200,
                "Goals fetched successfully",
                goals
        );
    }

    // ================= UPDATE GOAL PROGRESS =================
    @Override
    public ApiResponse updateGoalProgress(Long goalId, Integer progress) {

        Goal goal = goalRepo.findById(goalId)
                .orElseThrow(() ->
                        new GoalUpdateException("Goal not found"));

        if (progress == null || progress < 0 || progress > 100) {
            throw new GoalUpdateException(
                    "Progress must be between 0 and 100");
        }

        goal.setProgress(progress);

        if (progress == 0)
            goal.setStatus("NOT_STARTED");
        else if (progress == 100)
            goal.setStatus("COMPLETED");
        else
            goal.setStatus("IN_PROGRESS");

        goalRepo.save(goal);

        return new ApiResponse(
                200,
                "Goal progress updated successfully",
                null
        );
    }

    // ================= DELETE GOAL =================
    @Override
    public ApiResponse deleteGoal(Long goalId) {

        Goal goal = goalRepo.findById(goalId)
                .orElseThrow(() ->
                        new GoalUpdateException("Goal not found"));

        if ("COMPLETED".equalsIgnoreCase(goal.getStatus())) {
            throw new GoalUpdateException(
                    "Completed goal cannot be deleted");
        }

        goalRepo.delete(goal);

        return new ApiResponse(
                200,
                "Goal deleted successfully",
                null
        );
    }
}