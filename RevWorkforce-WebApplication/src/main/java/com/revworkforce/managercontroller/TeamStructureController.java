package com.revworkforce.managercontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.managerservice.TeamStructureService;

@RestController
@RequestMapping("/manager/team")
public class TeamStructureController {

	private final TeamStructureService service;

	public TeamStructureController(TeamStructureService service) {
		this.service = service;
	}

	@GetMapping("/{managerId}")
	public ResponseEntity<ApiResponse> getTeamStructure(@PathVariable Long managerId) {

		return ResponseEntity.ok(service.getTeamStructure(managerId));
	}

	@GetMapping("/{managerId}/profile/{employeeId}")
	public ResponseEntity<ApiResponse> getProfile(@PathVariable Long managerId, @PathVariable Long employeeId) {

		return ResponseEntity.ok(service.getTeamMemberProfile(managerId, employeeId));
	}

}