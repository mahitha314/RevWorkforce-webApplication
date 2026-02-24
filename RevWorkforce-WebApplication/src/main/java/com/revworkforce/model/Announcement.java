package com.revworkforce.model;

<<<<<<< HEAD
import jakarta.persistence.*;
import java.time.LocalDate;
=======
import java.time.LocalDate;
import jakarta.persistence.*;
>>>>>>> dev

@Entity
@Table(name = "announcements")
public class Announcement {

<<<<<<< HEAD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announcementId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "message", nullable = false, length = 2000)
    private String message;

    @Column(name = "posted_date")
    private LocalDate postedDate;

    public Announcement() {
    }

    public Announcement(String title, String message, LocalDate postedDate) {
        this.title = title;
        this.message = message;
        this.postedDate = postedDate;
    }


    public Long getAnnouncementId() { return announcementId; }
    public void setAnnouncementId(Long announcementId) { this.announcementId = announcementId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDate getPostedDate() { return postedDate; }
    public void setPostedDate(LocalDate postedDate) { this.postedDate = postedDate; }
}
=======
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String message;

	@Column(name = "posted_date")
	private LocalDate postedDate;

	public Announcement() {}

	public Announcement(Long id, String title, String message, LocalDate postedDate) {
		this.id = id;
		this.title = title;
		this.message = message;
		this.postedDate = postedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDate getPostedDate() {
		return postedDate;
	}

	public void setPostedDate(LocalDate postedDate) {
		this.postedDate = postedDate;
	}

}
>>>>>>> dev
