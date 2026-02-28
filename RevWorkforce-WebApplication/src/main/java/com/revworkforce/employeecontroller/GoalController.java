package com.revworkforce.employeecontroller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.dto.GoalDTO;
import com.revworkforce.employeeservice.GoalService;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employee/goals")
public class GoalController {

    private final GoalService goalService;
    private final EmployeeRepository employeeRepository;

    public GoalController(GoalService goalService,
                          EmployeeRepository employeeRepository) {
        this.goalService = goalService;
        this.employeeRepository = employeeRepository;
    }

    // =============================
    // LOAD GOALS PAGE
    // =============================
    @GetMapping({"","/"})
    public String showGoalsPage(Model model, Principal principal) {

        Employee employee = employeeRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        Long employeeId = employee.getId();
        model.addAttribute("employee",employee);

        model.addAttribute("goal", new GoalDTO());
        model.addAttribute("goals",
                goalService.getEmployeeGoals(employeeId));

        return "employee/goals";
    }

    // =============================
    // CREATE GOAL
    // =============================
    @PostMapping
    public String createGoalFromUI(@ModelAttribute("goal") GoalDTO goalDTO,
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {

        Employee employee = employeeRepository
                .findByEmail(principal.getName())
                .orElseThrow();

        goalService.createGoal(employee.getId(), goalDTO);

        redirectAttributes.addFlashAttribute("successMessage",
                "Goal added successfully!");

        return "redirect:/employee/goals";
    }

    // =============================
    // UPDATE STATUS
    // =============================
    @GetMapping("/status/{goalId}")
    public String updateStatus(@PathVariable Long goalId,
                               @RequestParam String status,
                               Principal principal) {

        goalService.updateGoalStatus(goalId, status);

        return "redirect:/employee/goals";
    }

    // =============================
    // DELETE GOAL
    // =============================
    @GetMapping("/delete/{goalId}")
    public String deleteGoal(@PathVariable Long goalId) {

        goalService.deleteGoal(goalId);

        return "redirect:/employee/goals";
    }
}
