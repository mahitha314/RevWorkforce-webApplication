package com.revworkforce.model;

<<<<<<< HEAD
import jakarta.persistence.*;
import java.time.LocalDate;
=======
import java.time.LocalDate;
import jakarta.persistence.*;
>>>>>>> dev

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

<<<<<<< HEAD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String leaveType; // Casual, Sick, Paid

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String status; // PENDING / APPROVED / REJECTED / CANCELLED

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public LeaveRequest() {}

    public LeaveRequest(Long id, LocalDate startDate, LocalDate endDate,
                        String leaveType, String reason, String status, Employee employee) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
        this.reason = reason;
        this.status = status;
        this.employee = employee;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}
=======
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
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "leave_type_id", nullable = false)
	private LeaveType leaveType;

	@OneToOne
	@JoinColumn(name = "approval_id")
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
>>>>>>> dev
