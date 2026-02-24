package com.revworkforce.employeeservice;

<<<<<<< HEAD
import org.springframework.stereotype.Service;

import com.revworkforce.dto.GoalDTO;
import com.revworkforce.exception.GoalUpdateException;
import com.revworkforce.model.Employee;
import com.revworkforce.model.Goal;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.GoalRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final EmployeeRepository employeeRepository;

    public GoalService(GoalRepository goalRepository,
                       EmployeeRepository employeeRepository) {
        this.goalRepository = goalRepository;
        this.employeeRepository = employeeRepository;
    }

    
    public GoalDTO createGoal(Long employeeId, GoalDTO dto) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Goal goal = new Goal();
        goal.setEmployee(employee);
        goal.setGoalDescription(dto.getGoalDescription());
        goal.setPriority(dto.getPriority());
        goal.setStatus("Not Started");
        goal.setDeadline(dto.getDeadline());

        Goal saved = goalRepository.save(goal);

        return mapToDTO(saved);
    }

    
    public GoalDTO updateGoal(Long goalId, GoalDTO dto) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GoalUpdateException("Goal not found"));

        if ("Completed".equals(goal.getStatus())) {
            throw new GoalUpdateException("Cannot update completed goal");
        }

        goal.setGoalDescription(dto.getGoalDescription());
        goal.setPriority(dto.getPriority());
        goal.setDeadline(dto.getDeadline());

        Goal updated = goalRepository.save(goal);

        return mapToDTO(updated);
    }

   
    public List<GoalDTO> getEmployeeGoals(Long employeeId) {

        
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return goalRepository.findByEmployeeEmployeeId(employeeId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

        public GoalDTO updateGoalStatus(Long goalId, String status) {

        List<String> validStatuses =
                Arrays.asList("Not Started", "In Progress", "Completed");

        if (!validStatuses.contains(status)) {
            throw new GoalUpdateException("Invalid goal status");
        }

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new GoalUpdateException("Goal not found"));

        goal.setStatus(status);

        Goal updated = goalRepository.save(goal);

        return mapToDTO(updated);
    }

    
    private GoalDTO mapToDTO(Goal goal) {

        GoalDTO dto = new GoalDTO();
        dto.setGoalId(goal.getGoalId());
        dto.setGoalDescription(goal.getGoalDescription());
        dto.setPriority(goal.getPriority());
        dto.setStatus(goal.getStatus());
        dto.setDeadline(goal.getDeadline());

        return dto;
    }
}
=======
public class GoalService {

}
>>>>>>> dev
