package com.revworkforce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class LeaveApprovalDTO {

    @NotNull
    private Long leaveRequestId;

    @NotNull
    private Long managerId;

    @NotBlank
    private String status; 

    private String comments;

    public LeaveApprovalDTO() {}

    public LeaveApprovalDTO(Long leaveRequestId,
                            Long managerId,
                            String status,
                            String comments) {
        this.leaveRequestId = leaveRequestId;
        this.managerId = managerId;
        this.status = status;
        this.comments = comments;
    }

    public Long getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(Long leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
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
    
}