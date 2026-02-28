package com.revworkforce.employeeservice;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revworkforce.dto.GoalDTO;
import com.revworkforce.exception.GoalUpdateException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Goal;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.GoalRepository;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final EmployeeRepository employeeRepository;

    public GoalService(GoalRepository goalRepository,
                       EmployeeRepository employeeRepository) {
        this.goalRepository = goalRepository;
        this.employeeRepository = employeeRepository;
    }

    // =============================
    // ✅ CREATE GOAL
    // =============================
    public GoalDTO createGoal(Long employeeId, GoalDTO dto) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Goal goal = new Goal();
        goal.setEmployee(employee);
        goal.setGoalDescription(dto.getGoalDescription());
        goal.setPriority(dto.getPriority());
        goal.setStatus("Not Started");
        goal.setDeadline(dto.getDeadline());
        goal.setProgress(0);

        Goal saved = goalRepository.save(goal);

        return mapToDTO(saved);
    }

    // =============================
    // ✅ UPDATE GOAL DETAILS
    // =============================
    public GoalDTO updateGoal(Long goalId, GoalDTO dto) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() ->
                        new GoalUpdateException("Goal not found"));

        if ("Completed".equals(goal.getStatus())) {
            throw new GoalUpdateException(
                    "Cannot update completed goal");
        }

        goal.setGoalDescription(dto.getGoalDescription());
        goal.setPriority(dto.getPriority());
        goal.setDeadline(dto.getDeadline());

        return mapToDTO(goalRepository.save(goal));
    }

    // =============================
    // ✅ GET EMPLOYEE GOALS
    // =============================
    public List<GoalDTO> getEmployeeGoals(Long employeeId) {

        employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        return goalRepository.findByEmployee_Id(employeeId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // =============================
    // ✅ UPDATE GOAL STATUS
    // =============================
    public GoalDTO updateGoalStatus(Long goalId, String status) {

        List<String> validStatus =
                Arrays.asList(
                        "Not Started",
                        "In Progress",
                        "Completed"
                );

        if (!validStatus.contains(status)) {
            throw new GoalUpdateException("Invalid status");
        }

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() ->
                        new GoalUpdateException("Goal not found"));

        goal.setStatus(status);

        return mapToDTO(goalRepository.save(goal));
    }

    // =============================
    // ✅ DELETE GOAL (EMPLOYEE)
    // =============================
    public void deleteGoal(Long goalId) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() ->
                        new GoalUpdateException("Goal not found"));

        // Optional rule
        if ("Completed".equals(goal.getStatus())) {
            throw new GoalUpdateException(
                    "Completed goal cannot be deleted");
        }

        goalRepository.delete(goal);
    }

    // =============================
    // DTO MAPPINGw
    // =============================
    private GoalDTO mapToDTO(Goal goal) {

        GoalDTO dto = new GoalDTO();

        dto.setGoalId(goal.getId());
        dto.setGoalDescription(goal.getGoalDescription());
        dto.setPriority(goal.getPriority());
        dto.setStatus(goal.getStatus());
        dto.setDeadline(goal.getDeadline());
        //dto.setProgress(goal.getProgress());

        return dto;
    }
}
