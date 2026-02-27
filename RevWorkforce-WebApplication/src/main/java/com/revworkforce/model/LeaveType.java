package com.revworkforce.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_types")
public class LeaveType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String typeName;

	@Column(nullable = false)
	private int totalDays;

	@OneToMany(mappedBy = "leaveType")
	@JsonManagedReference(value = "leavetype-leavebalance")
	private List<LeaveBalance> leaveBalances;

	@OneToMany(mappedBy = "leaveType")
	@JsonManagedReference(value = "leavetype-leaverequest")
	private List<LeaveRequest> leaveRequests;

	public LeaveType() {}

	public LeaveType(Long id, String typeName, int totalDays) {
		this.id = id;
		this.typeName = typeName;
		this.totalDays = totalDays;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public List<LeaveBalance> getLeaveBalances() {
		return leaveBalances;
	}

	public void setLeaveBalances(List<LeaveBalance> leaveBalances) {
		this.leaveBalances = leaveBalances;
	}

	public List<LeaveRequest> getLeaveRequests() {
		return leaveRequests;
	}

	public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}

}