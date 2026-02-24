package com.revworkforce.model;

<<<<<<< HEAD
import jakarta.persistence.*;
import java.time.LocalDateTime;
=======
import java.time.LocalDateTime;
import jakarta.persistence.*;
>>>>>>> dev

@Entity
@Table(name = "notifications")
public class Notification {

<<<<<<< HEAD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Notification() {
    }

    public Notification(Employee employee, String message, boolean isRead, LocalDateTime createdAt) {
        this.employee = employee;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getNotificationId() { return notificationId; }
    public void setNotificationId(Long notificationId) { this.notificationId = notificationId; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
=======
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notificationId;

	@Column(name = "message", nullable = false, length = 1000)
	private String message;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	public Notification() {}

	public Notification(Long notificationId, String message, String status, LocalDateTime createdAt,
			Employee employee) {
		this.notificationId = notificationId;
		this.message = message;
		this.status = status;
		this.createdAt = createdAt;
		this.employee = employee;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

}
>>>>>>> dev
