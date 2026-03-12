package com.revworkforce.employeecontroller;

import com.revworkforce.dto.*;
import com.revworkforce.employeeservice.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
@RequestMapping("/api/employee")
public class LeaveController {

    private final LeaveRequestService leaveRequestService;
    private final LeaveBalanceService leaveBalanceService;
    private final HolidayService holidayService;

    public LeaveController(
            LeaveRequestService leaveRequestService,
            LeaveBalanceService leaveBalanceService,
            HolidayService holidayService) {

        this.leaveRequestService = leaveRequestService;
        this.leaveBalanceService = leaveBalanceService;
        this.holidayService = holidayService;
    }

    @PostMapping("/leave/apply")
    public ResponseEntity<ApiResponse> applyLeave(
            @RequestBody LeaveRequestDTO dto) {

        ApiResponse response = leaveRequestService.applyLeave(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/leave/cancel/{leaveId}")
    public ResponseEntity<ApiResponse> cancelLeave(
            @PathVariable Long leaveId) {

        ApiResponse response = leaveRequestService.cancelLeave(leaveId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/leave/history/{employeeId}")
    public ResponseEntity<ApiResponse> getHistory(
            @PathVariable Long employeeId) {

        ApiResponse response =
                leaveRequestService.getLeaveHistory(employeeId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/leave/balance/{employeeId}")
    public ResponseEntity<ApiResponse> getBalances(
            @PathVariable Long employeeId) {

        ApiResponse response =
                leaveBalanceService.getEmployeeBalances(employeeId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/holidays")
    public ResponseEntity<ApiResponse> getHolidays() {

        ApiResponse response = holidayService.getAllHolidays();
        return ResponseEntity.ok(response);
    }
}