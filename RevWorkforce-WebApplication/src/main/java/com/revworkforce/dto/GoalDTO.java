package com.revworkforce.dto;

import java.time.LocalDate;

public class GoalDTO {

	private Long goalId;
	private Long employeeId;
	private String employeeName;

	private String goalDescription;
	private String priority;
	private String status;
	private LocalDate deadline;
	private Integer progress;

	public GoalDTO() {
	}

	public GoalDTO(Long goalId, Long employeeId, String employeeName, String goalDescription, String priority,
			String status, LocalDate deadline, Integer progress) {

		this.goalId = goalId;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.goalDescription = goalDescription;
		this.priority = priority;
		this.status = status;
		this.deadline = deadline;
		this.progress = progress;
	}

	public Long getGoalId() {
		return goalId;
	}

	public void setGoalId(Long goalId) {
		this.goalId = goalId;
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

	public String getGoalDescription() {
		return goalDescription;
	}

	public void setGoalDescription(String goalDescription) {
		this.goalDescription = goalDescription;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
}