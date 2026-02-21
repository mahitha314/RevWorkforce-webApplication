package com.workforce.managercontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.workforce.managerservice.TeamStructureService;

@Controller
@RequestMapping("/manager/team-structure")
public class TeamStructureController {

	@Autowired
	private TeamStructureService teamStructureService;

	@GetMapping
	public String viewTeamStructure(Model model) {
		Long managerId = 1L;

		model.addAttribute("team", teamStructureService.getTeamMembers(managerId));

		return "manager/team_structure";
	}
}
