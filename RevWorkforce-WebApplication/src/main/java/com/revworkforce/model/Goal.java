package com.revworkforce.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "goals")
public class Goal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String goalTitle;

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

	public Goal(Long id, String goalTitle, String goalDescription, String priority, String status, LocalDate deadline,
			int progress, Employee employee) {
		super();
		this.id = id;
		this.goalTitle = goalTitle;
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

	public String getGoalTitle() {
		return goalTitle;
	}

	public void setGoalTitle(String goalTitle) {
		this.goalTitle = goalTitle;
	}

}