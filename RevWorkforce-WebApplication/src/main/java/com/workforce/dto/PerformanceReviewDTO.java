package com.workforce.dto;

public class PerformanceReviewDTO {

	private Long reviewId;
	private Long employeeId;
	private String employeeName;

	private int selfRating;
	private int managerRating;

	private String managerFeedback;
	private String status;

	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public int getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(int selfRating) {
		this.selfRating = selfRating;
	}

	public int getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(int managerRating) {
		this.managerRating = managerRating;
	}

	public String getManagerFeedback() {
		return managerFeedback;
	}

	public void setManagerFeedback(String managerFeedback) {
		this.managerFeedback = managerFeedback;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
