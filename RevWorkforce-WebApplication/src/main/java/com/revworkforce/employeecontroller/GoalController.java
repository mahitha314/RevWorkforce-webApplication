package com.revworkforce.employeecontroller;

<<<<<<< HEAD
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.dto.GoalDTO;
import com.revworkforce.employeeservice.GoalService;

@Controller
@RequestMapping("/employee/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("/{employeeId}")
    public String showGoalsPage(@PathVariable Long employeeId, Model model) {

        model.addAttribute("employeeId", employeeId);
        model.addAttribute("goal", new GoalDTO());
        model.addAttribute("goals", goalService.getEmployeeGoals(employeeId));

        return "employee/goals";  
    }

  
    @PostMapping("/{employeeId}")
    public String createGoal(@PathVariable Long employeeId,
                             @ModelAttribute("goal") GoalDTO goalDTO) {

        goalService.createGoal(employeeId, goalDTO);

        return "redirect:/employee/goals/" + employeeId;
    }

   
    @GetMapping("/{employeeId}/status/{goalId}")
    public String updateStatus(@PathVariable Long employeeId,
                               @PathVariable Long goalId,
                               @RequestParam String status) {

        goalService.updateGoalStatus(goalId, status);

        return "redirect:/employee/goals/" + employeeId;
    }
}
=======
public class GoalController {

}
>>>>>>> dev
