package com.revworkforce.managerservice;

import com.revworkforce.dto.ApiResponse;

public interface TeamStructureService {

	ApiResponse getTeamStructure(Long managerId);

	ApiResponse getTeamMemberProfile(Long managerId, Long employeeId);

}