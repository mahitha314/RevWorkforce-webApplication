package com.revworkforce.dto;

import java.time.LocalDate;

public class GoalDTO {

    private Long goalId;
    private String goalDescription;
    private String priority;
    private String status;
    private LocalDate deadline;

    public Long getGoalId() { return goalId; }
    public void setGoalId(Long goalId) { this.goalId = goalId; }

    public String getGoalDescription() { return goalDescription; }
    public void setGoalDescription(String goalDescription) { this.goalDescription = goalDescription; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}