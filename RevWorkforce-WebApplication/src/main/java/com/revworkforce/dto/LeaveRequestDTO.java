
package com.revworkforce.dto;

import java.time.LocalDate;

public class LeaveRequestDTO {

    private Long id;

    private Long employeeId;
    private String employeeName;

    private Long leaveTypeId;
    private String leaveTypeName;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;

    private String reason;
    private String status;  // PENDING / APPROVED / REJECTED

    public LeaveRequestDTO() {}

    public LeaveRequestDTO(Long id,
                           Long employeeId,
                           String employeeName,
                           Long leaveTypeId,
                           String leaveTypeName,
                           LocalDate startDate,
                           LocalDate endDate,
                           Integer totalDays,
                           String reason,
                           String status) {

        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeName = leaveTypeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.reason = reason;
        this.status = status;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(Long leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public String getLeaveTypeName() {
		return leaveTypeName;
	}

	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Integer getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(Integer totalDays) {
		this.totalDays = totalDays;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
