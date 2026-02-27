package com.revworkforce.admincontroller;

import com.revworkforce.adminservice.LeaveManagementService;
import com.revworkforce.dto.AdjustLeaveDTO;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/leaves")
public class LeaveManagementController {

    private final LeaveManagementService leaveService;

    public LeaveManagementController(LeaveManagementService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/type")
    public ResponseEntity<ApiResponse> createLeaveType(@RequestBody LeaveTypeDTO dto) {

        ApiResponse response = leaveService.createLeaveType(dto);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse> getAllLeaveTypes() {

        ApiResponse response = leaveService.getAllLeaveTypes();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse> assignLeave(@RequestBody LeaveBalanceDTO dto) {

        ApiResponse response = leaveService.assignLeave(dto);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @PutMapping("/adjust")
    public ResponseEntity<ApiResponse> adjustLeave(@RequestBody AdjustLeaveDTO dto) {

        ApiResponse response = leaveService.adjustLeave(dto);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllEmployeeLeaves() {

        ApiResponse response = leaveService.getAllEmployeeLeaves();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/employee/{empId}")
    public ResponseEntity<ApiResponse> getEmployeeLeave(@PathVariable Long empId) {

        ApiResponse response = leaveService.getEmployeeLeave(empId);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<ApiResponse> getDepartmentReport(@PathVariable Long deptId) {

        ApiResponse response = leaveService.getDepartmentReport(deptId);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
    
}