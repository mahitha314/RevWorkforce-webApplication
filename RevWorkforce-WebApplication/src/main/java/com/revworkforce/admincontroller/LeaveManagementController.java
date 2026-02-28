package com.revworkforce.admincontroller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.revworkforce.adminservice.LeaveManagementService;
import com.revworkforce.dto.*;
import com.revworkforce.model.LeaveBalance;

@RestController
@RequestMapping("/api/admin/leave")   // ✅ Better REST structure
public class LeaveManagementController {

    private final LeaveManagementService service;

    public LeaveManagementController(LeaveManagementService service) {
        this.service = service;
    }

    // ================= LEAVE TYPE =================
    @PostMapping("/type")
    public LeaveTypeDTO createLeaveType(@RequestBody LeaveTypeDTO dto) {
        return service.createLeaveType(dto);
    }

    @GetMapping("/type")
    public List<LeaveTypeDTO> getAllTypes() {
        return service.getAllLeaveTypes();
    }

    // ================= ASSIGN =================
    @PostMapping("/assign")
    public LeaveBalance assignLeave(@RequestParam Long employeeId,
                                    @RequestParam Long leaveTypeId,
                                    @RequestParam int totalDays) {

        return service.assignLeaveToEmployee(employeeId, leaveTypeId, totalDays);
    }

    // ================= ADJUST =================
    @PostMapping("/adjust")
    public LeaveBalance adjustLeave(@RequestBody LeaveBalanceDTO dto) {
        return service.adjustLeave(dto);
    }

    // ================= REPORTS =================
    @GetMapping("/employee/{id}")
    public List<LeaveBalance> employeeLeave(@PathVariable Long id) {
        return service.getEmployeeLeaveInfo(id);
    }

    @GetMapping("/department/{id}")
    public List<LeaveBalance> departmentReport(@PathVariable Long id) {
        return service.getDepartmentLeaveReport(id);
    }
}