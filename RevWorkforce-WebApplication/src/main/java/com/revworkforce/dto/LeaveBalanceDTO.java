package com.revworkforce.dto;

public class LeaveBalanceDTO {

	private Long id;

	private Long employeeId;
	private String employeeName;

	private Long leaveTypeId;
	private String leaveTypeName;

	private Integer totalLeaves;
	private Integer usedLeaves;
	private Integer remainingLeaves;

	private Integer days;
	private String reason;

	public LeaveBalanceDTO() {
	}

	public LeaveBalanceDTO(Long id, Long employeeId, String employeeName, Long leaveTypeId, String leaveTypeName,
			Integer totalLeaves, Integer usedLeaves, Integer remainingLeaves) {

		this.id = id;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.leaveTypeId = leaveTypeId;
		this.leaveTypeName = leaveTypeName;
		this.totalLeaves = totalLeaves;
		this.usedLeaves = usedLeaves;
		this.remainingLeaves = remainingLeaves;
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

	public Integer getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(Integer totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	public Integer getUsedLeaves() {
		return usedLeaves;
	}

	public void setUsedLeaves(Integer usedLeaves) {
		this.usedLeaves = usedLeaves;
	}

	public Integer getRemainingLeaves() {
		return remainingLeaves;
	}

	public void setRemainingLeaves(Integer remainingLeaves) {
		this.remainingLeaves = remainingLeaves;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}