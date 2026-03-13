package com.revworkforce.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "leave_approvals")
public class LeaveApproval {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "manager_id", nullable = false)
	private Employee manager;

	@Column(nullable = false)
	private String status;

	@Column(name = "comments")
	private String comments;

	@Column(name = "approval_date")
	private LocalDate approvalDate;

	@OneToOne(mappedBy = "leaveApproval")
	@JsonBackReference(value = "request-approval")
	private LeaveRequest leaveRequest;

	public LeaveApproval() {
	}

	public LeaveApproval(Long id, Employee manager, String status, String comments, LocalDate approvalDate) {
		this.id = id;
		this.manager = manager;
		this.status = status;
		this.comments = comments;
		this.approvalDate = approvalDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LocalDate getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(LocalDate approvalDate) {
		this.approvalDate = approvalDate;
	}

	public LeaveRequest getLeaveRequest() {
		return leaveRequest;
	}

	public void setLeaveRequest(LeaveRequest leaveRequest) {
		this.leaveRequest = leaveRequest;
	}

}