package com.revworkforce.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column(nullable = false)
	private String reason;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	@JsonBackReference(value = "employee-leaverequest")
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "leave_type_id", nullable = false)
	@JsonBackReference(value = "leavetype-leaverequest")
	private LeaveType leaveType;

	@OneToOne
	@JoinColumn(name = "approval_id")
	@JsonManagedReference(value="request-approval")
	private LeaveApproval leaveApproval;

	public LeaveRequest() {}

	public LeaveRequest(Long id, LocalDate startDate, LocalDate endDate, String reason, Employee employee,
			LeaveType leaveType, LeaveApproval leaveApproval) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
		this.employee = employee;
		this.leaveType = leaveType;
		this.leaveApproval = leaveApproval;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public LeaveApproval getLeaveApproval() {
		return leaveApproval;
	}

	public void setLeaveApproval(LeaveApproval leaveApproval) {
		this.leaveApproval = leaveApproval;
	}
	
}