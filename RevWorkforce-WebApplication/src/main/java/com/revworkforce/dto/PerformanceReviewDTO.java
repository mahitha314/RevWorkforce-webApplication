package com.revworkforce.dto;

import java.time.LocalDate;

public class PerformanceReviewDTO {

	private Long reviewId;
	private Long employeeId;
	private String employeeName;

	private String accomplishments;
	private String deliverables;
	private String areasOfImprovement;

	private Integer selfRating; 
	private Integer managerRating; 

	private String managerFeedback;
	private String status;
	private LocalDate submittedDate;

	public PerformanceReviewDTO() {
	}

	public PerformanceReviewDTO(Long reviewId, Long employeeId, String employeeName, String accomplishments,
			String deliverables, String areasOfImprovement, Integer selfRating, Integer managerRating,
			String managerFeedback, String status, LocalDate submittedDate) {

		this.reviewId = reviewId;
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.accomplishments = accomplishments;
		this.deliverables = deliverables;
		this.areasOfImprovement = areasOfImprovement;
		this.selfRating = selfRating;
		this.managerRating = managerRating;
		this.managerFeedback = managerFeedback;
		this.status = status;
		this.submittedDate = submittedDate;
	}

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

	public String getAccomplishments() {
		return accomplishments;
	}

	public void setAccomplishments(String accomplishments) {
		this.accomplishments = accomplishments;
	}

	public String getDeliverables() {
		return deliverables;
	}

	public void setDeliverables(String deliverables) {
		this.deliverables = deliverables;
	}

	public String getAreasOfImprovement() {
		return areasOfImprovement;
	}

	public void setAreasOfImprovement(String areasOfImprovement) {
		this.areasOfImprovement = areasOfImprovement;
	}

	public Integer getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(Integer selfRating) { 
		this.selfRating = selfRating;
	}

	public Integer getManagerRating() { 
		return managerRating;
	}

	public void setManagerRating(Integer managerRating) {
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

	public LocalDate getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(LocalDate submittedDate) {
		this.submittedDate = submittedDate;
	}
}