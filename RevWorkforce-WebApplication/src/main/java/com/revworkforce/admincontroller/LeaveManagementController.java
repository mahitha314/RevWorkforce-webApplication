package com.revworkforce.admincontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.adminservice.LeaveManagementService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.LeaveBalanceDTO;
import com.revworkforce.dto.LeaveTypeDTO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;

@RestController
@RequestMapping("/api/admin/leave")
public class LeaveManagementController {

    private final LeaveManagementService service;

    public LeaveManagementController(LeaveManagementService service) {
        this.service = service;
    }

    @PostMapping("/type")
    public ResponseEntity<ApiResponse> createLeaveType(@RequestBody LeaveTypeDTO dto) {
        try {
            LeaveTypeDTO created = service.createLeaveType(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(201, "Leave type added successfully", created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Failed to create leave type", null));
        }
    }

    @GetMapping("/type")
    public ResponseEntity<ApiResponse> getAllTypes() {
        try {
            List<LeaveTypeDTO> leaveTypes = service.getAllLeaveTypes();
            return ResponseEntity.ok(new ApiResponse(200, "Leave types fetched successfully", leaveTypes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Failed to fetch leave types", null));
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse> assignLeave(@RequestParam Long employeeId,
                                                   @RequestParam Long leaveTypeId,
                                                   @RequestParam int totalDays) {
        try {
            LeaveBalance assigned = service.assignLeaveToEmployee(employeeId, leaveTypeId, totalDays);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(201, "Leave assigned successfully", assigned));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Failed to assign leave", null));
        }
    }

    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse> adjustLeave(@RequestBody LeaveBalanceDTO dto) {
        try {
            LeaveBalance adjusted = service.adjustLeave(dto);
            return ResponseEntity.ok(new ApiResponse(200, "Leave adjusted successfully", adjusted));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Failed to adjust leave", null));
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<ApiResponse> employeeLeave(@PathVariable Long id) {
        try {
            List<LeaveBalance> balances = service.getEmployeeLeaveInfo(id);
            return ResponseEntity.ok(new ApiResponse(200, "Employee leave details fetched successfully", balances));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Failed to fetch employee leave details", null));
        }
    }

    @GetMapping("/report/department/{departmentId}")
    public ResponseEntity<ApiResponse> getDepartmentLeaveReport(@PathVariable Long departmentId) {
        try {
            List<LeaveBalance> report = service.getDepartmentLeaveReport(departmentId);
            return ResponseEntity.ok(new ApiResponse(200, "Department leave report fetched successfully", report));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Failed to fetch department leave report", null));
        }
    }

    @GetMapping("/available-employees/{leaveTypeId}")
    public ResponseEntity<ApiResponse> getAvailableEmployees(@PathVariable Long leaveTypeId) {
        try {
            List<Employee> employees = service.getEmployeesNotAssignedToLeaveType(leaveTypeId);
            return ResponseEntity.ok(new ApiResponse(200, "Available employees fetched successfully", employees));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(500, "Failed to fetch available employees", null));
        }
    }
}