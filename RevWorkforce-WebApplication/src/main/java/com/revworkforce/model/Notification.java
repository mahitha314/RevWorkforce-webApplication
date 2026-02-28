package com.revworkforce.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationId;
	
	@Column(name = "title", length = 255)
	private String title;

	@Column(name = "message", nullable = false, length = 1000)
	private String message;
	
	@Column(name = "type", length = 100)
	private String type;

	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "is_read")
	private Boolean isRead = false;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;
	
	@Column(name = "reference_id")
	private Long referenceId;

	public Notification() {}

	public Notification(Long notificationId, String title, String message, String type, String status, Boolean isRead,
			LocalDateTime createdAt, Employee employee, Long referenceId) {
		super();
		this.notificationId = notificationId;
		this.title = title;
		this.message = message;
		this.type = type;
		this.status = status;
		this.isRead = isRead;
		this.createdAt = createdAt;
		this.employee = employee;
		this.referenceId = referenceId;
	}

	public Long getNotificationId() {
		return notificationId;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

}