package com.revworkforce.dto;

public class DashboardDTO {

    private double goalPercentage;
    private int totalAllocated;
    private int leavesUsed;
    private int remainingLeaves;
    private long pendingRequests;
    private double performanceRating;

    public DashboardDTO() {
    }

    public DashboardDTO(double goalPercentage,
                        int totalAllocated,
                        int leavesUsed,
                        int remainingLeaves,
                        long pendingRequests,
                        double performanceRating) {

        this.goalPercentage = goalPercentage;
        this.totalAllocated = totalAllocated;
        this.leavesUsed = leavesUsed;
        this.remainingLeaves = remainingLeaves;
        this.pendingRequests = pendingRequests;
        this.performanceRating = performanceRating;
    }

    public double getGoalPercentage() {
        return goalPercentage;
    }

    public void setGoalPercentage(double goalPercentage) {
        this.goalPercentage = goalPercentage;
    }

    public int getTotalAllocated() {
        return totalAllocated;
    }

    public void setTotalAllocated(int totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public int getLeavesUsed() {
        return leavesUsed;
    }

    public void setLeavesUsed(int leavesUsed) {
        this.leavesUsed = leavesUsed;
    }

    public int getRemainingLeaves() {
        return remainingLeaves;
    }

    public void setRemainingLeaves(int remainingLeaves) {
        this.remainingLeaves = remainingLeaves;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(long pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public double getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(double performanceRating) {
        this.performanceRating = performanceRating;
    }

    @Override
    public String toString() {
        return "DashboardDTO{" +
                "goalPercentage=" + goalPercentage +
                ", totalAllocated=" + totalAllocated +
                ", leavesUsed=" + leavesUsed +
                ", remainingLeaves=" + remainingLeaves +
                ", pendingRequests=" + pendingRequests +
                ", performanceRating=" + performanceRating +
                '}';
    }
}