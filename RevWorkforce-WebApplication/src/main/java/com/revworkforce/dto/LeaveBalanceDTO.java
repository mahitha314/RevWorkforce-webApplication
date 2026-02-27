package com.revworkforce.dto;

public class LeaveBalanceDTO {

    private Long leaveTypeId;
    private String leaveTypeName;
    private int totalLeaves;
    private int usedLeaves;
    private int remainingLeaves;

    public LeaveBalanceDTO() {
    }

    public LeaveBalanceDTO(Long leaveTypeId,
                           String leaveTypeName,
                           int totalLeaves,
                           int usedLeaves,
                           int remainingLeaves) {
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeName = leaveTypeName;
        this.totalLeaves = totalLeaves;
        this.usedLeaves = usedLeaves;
        this.remainingLeaves = remainingLeaves;
    }

    public Long getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(Long leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
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