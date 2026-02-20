package com.workforce.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "goal_description", length = 1000)
    private String goalDescription;

    @Column(name = "priority", length = 20)
    private String priority; 

    @Column(name = "status", length = 30)
    private String status; 

    @Column(name = "deadline")
    private LocalDate deadline;

    public Goal() {}

    public Goal(Employee employee, String goalDescription, String priority,
                String status, LocalDate deadline) {
        this.employee = employee;
        this.goalDescription = goalDescription;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
    }

    public Long getGoalId() { return goalId;}
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
