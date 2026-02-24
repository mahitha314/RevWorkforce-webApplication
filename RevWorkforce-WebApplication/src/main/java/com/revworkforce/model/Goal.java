package com.revworkforce.model;
<<<<<<< HEAD
import jakarta.persistence.*;
import java.time.LocalDate;
=======

import java.time.LocalDate;
import jakarta.persistence.*;
>>>>>>> dev

@Entity
@Table(name = "goals")
public class Goal {

<<<<<<< HEAD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "goal_description", length = 1000)
    private String goalDescription;

    @Column(name = "priority", length = 20)
    private String priority; // High / Medium / Low

    @Column(name = "status", length = 30)
    private String status; // Not Started / In Progress / Completed

    @Column(name = "deadline")
    private LocalDate deadline;

    public Goal() {
    }

    public Goal(Employee employee, String goalDescription, String priority,
                String status, LocalDate deadline) {
        this.employee = employee;
        this.goalDescription = goalDescription;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
    }


    public Long getGoalId() { return goalId; }
    public void setGoalId(Long goalId) { this.goalId = goalId; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public String getGoalDescription() { return goalDescription; }
    public void setGoalDescription(String goalDescription) { this.goalDescription = goalDescription; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}
=======
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String goalDescription;

	@Column(nullable = false)
	private String priority;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private LocalDate deadline;

	@Column(nullable = false)
	private int progress;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	public Goal() {}

	public Goal(Long id, String goalDescription, String priority, String status, LocalDate deadline, int progress,
			Employee employee) {
		this.id = id;
		this.goalDescription = goalDescription;
		this.priority = priority;
		this.status = status;
		this.deadline = deadline;
		this.progress = progress;
		this.employee = employee;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
>>>>>>> dev
