package com.revworkforce.model;

<<<<<<< HEAD

import java.time.LocalDate;

=======
import java.time.LocalDate;
>>>>>>> dev
import jakarta.persistence.*;

@Entity
@Table(name = "leave_approvals")
public class LeaveApproval {

<<<<<<< HEAD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalId;

    @OneToOne
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leaveRequest;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Employee manager;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // Approved / Rejected

    @Column(name = "comments", length = 500)
    private String comments;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    public LeaveApproval() {
    }

    public LeaveApproval(LeaveRequest leaveRequest, Employee manager, String status, String comments, LocalDate approvalDate) {
        this.leaveRequest = leaveRequest;
        this.manager = manager;
        this.status = status;
        this.comments = comments;
        this.approvalDate = approvalDate;
    }


    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
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
}
=======
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

	public LeaveApproval() {}

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

}
>>>>>>> dev
