package com.workforce.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "performance_reviews")
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "deliverables", length = 1000)
    private String deliverables;

    @Column(name = "accomplishments", length = 1000)
    private String accomplishments;

    @Column(name = "areas_of_improvement", length = 1000)
    private String areasOfImprovement;

    @Column(name = "self_rating")
    private int selfRating;

    @Column(name = "manager_rating")
    private int managerRating;

    @Column(name = "manager_feedback", length = 1000)
    private String managerFeedback;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "submitted_date")
    private LocalDate submittedDate;

    public PerformanceReview() {
    }

    public PerformanceReview(Employee employee, String deliverables, String accomplishments,
                             String areasOfImprovement, int selfRating,
                             int managerRating, String managerFeedback,
                             String status, LocalDate submittedDate) {

        this.employee = employee;
        this.deliverables = deliverables;
        this.accomplishments = accomplishments;
        this.areasOfImprovement = areasOfImprovement;
        this.selfRating = selfRating;
        this.managerRating = managerRating;
        this.managerFeedback = managerFeedback;
        this.status = status;
        this.submittedDate = submittedDate;
    }

    public Long getReviewId() { return reviewId; }
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public String getDeliverables() { return deliverables; }
    public void setDeliverables(String deliverables) { this.deliverables = deliverables; }

    public String getAccomplishments() { return accomplishments; }
    public void setAccomplishments(String accomplishments) { this.accomplishments = accomplishments; }

    public String getAreasOfImprovement() { return areasOfImprovement; }
    public void setAreasOfImprovement(String areasOfImprovement) { this.areasOfImprovement = areasOfImprovement; }

    public int getSelfRating() { return selfRating; }
    public void setSelfRating(int selfRating) { this.selfRating = selfRating; }

    public int getManagerRating() { return managerRating; }
    public void setManagerRating(int managerRating) { this.managerRating = managerRating; }

    public String getManagerFeedback() { return managerFeedback; }
    public void setManagerFeedback(String managerFeedback) { this.managerFeedback = managerFeedback; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getSubmittedDate() { return submittedDate; }
    public void setSubmittedDate(LocalDate submittedDate) { this.submittedDate = submittedDate; }
    
}