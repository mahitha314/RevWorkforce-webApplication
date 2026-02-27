package com.revworkforce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_balances")
public class LeaveBalance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int totalLeaves;

	@Column(nullable = false)
	private int usedLeaves;

	@Column(nullable = false)
	private int remainingLeaves;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	@JsonBackReference(value = "employee-leavebalance")
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "leave_type_id", nullable = false)
	@JsonBackReference(value = "leavetype-leavebalance")
	private LeaveType leaveType;

	public LeaveBalance() {}

	public LeaveBalance(Long id, int totalLeaves, int usedLeaves, int remainingLeaves, Employee employee,
			LeaveType leaveType) {
		this.id = id;
		this.totalLeaves = totalLeaves;
		this.usedLeaves = usedLeaves;
		this.remainingLeaves = remainingLeaves;
		this.employee = employee;
		this.leaveType = leaveType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(int totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	public int getUsedLeaves() {
		return usedLeaves;
	}

	public void setUsedLeaves(int usedLeaves) {
		this.usedLeaves = usedLeaves;
	}

	public int getRemainingLeaves() {
		return remainingLeaves;
	}

	public void setRemainingLeaves(int remainingLeaves) {
		this.remainingLeaves = remainingLeaves;
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

}