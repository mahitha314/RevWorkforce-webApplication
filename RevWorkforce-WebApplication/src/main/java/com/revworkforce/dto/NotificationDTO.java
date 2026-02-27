package com.revworkforce.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

public class NotificationDTO {

	private Long notificationId;
	private Long employeeId;

	@NotBlank(message = "Title is required")
	@Size(max = 255, message = "Title cannot exceed 255 characters")
	private String title;

	@NotBlank(message = "Message cannot be empty")
	@Size(min = 3, max = 1000, message = "Message must be between 3 and 1000 characters")
	private String message;

	private String type;

	@NotBlank(message = "Status is required")
	@Pattern(regexp = "SENT|DELIVERED|SEEN|ACTIVE|APPROVED|REJECTED", message = "Status must be valid (SENT, DELIVERED, SEEN, ACTIVE, APPROVED, REJECTED)")
	private String status;

	@NotNull(message = "Read status is required")
	@Min(value = 0, message = "isRead must be 0 or 1")
	@Max(value = 1, message = "isRead must be 0 or 1")
	private Boolean isRead;

	private LocalDateTime createdAt;

	private Long referenceId;

	public NotificationDTO() {
	}

	public NotificationDTO(Long notificationId, String title, String message, String type, String status,
			Boolean isRead, LocalDateTime createdAt, Long referenceId) {
		this.notificationId = notificationId;
		this.title = title;
		this.message = message;
		this.type = type;
		this.status = status;
		this.isRead = isRead;
		this.createdAt = createdAt;
		this.referenceId = referenceId;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

}