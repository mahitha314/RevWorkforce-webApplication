package com.revworkforce.model;

import jakarta.persistence.*;
<<<<<<< HEAD
=======

>>>>>>> dev
@Entity
@Table(name = "leave_balances")
public class LeaveBalance {

<<<<<<< HEAD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveBalanceId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "leave_type", nullable = false, length = 50)
    private String leaveType; // Casual, Sick, Paid

    @Column(name = "total_leaves", nullable = false)
    private int totalLeaves;

    @Column(name = "used_leaves", nullable = false)
    private int usedLeaves;

    @Column(name = "remaining_leaves", nullable = false)
    private int remainingLeaves;

    // Default Constructor
    public LeaveBalance() {
    }

    // Parameterized Constructor
    public LeaveBalance(Employee employee, String leaveType, int totalLeaves, int usedLeaves, int remainingLeaves) {
        this.employee = employee;
        this.leaveType = leaveType;
        this.totalLeaves = totalLeaves;
        this.usedLeaves = usedLeaves;
        this.remainingLeaves = remainingLeaves;
    }

    // Getters and Setters

    public Long getLeaveBalanceId() {
        return leaveBalanceId;
    }

    public void setLeaveBalanceId(Long leaveBalanceId) {
        this.leaveBalanceId = leaveBalanceId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
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
}
=======
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
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "leave_type_id", nullable = false)
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
>>>>>>> dev
