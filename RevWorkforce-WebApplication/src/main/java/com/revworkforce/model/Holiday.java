package com.revworkforce.model;

import java.time.LocalDate;
<<<<<<< HEAD

=======
>>>>>>> dev
import jakarta.persistence.*;

@Entity
@Table(name = "holidays")
public class Holiday {

<<<<<<< HEAD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holidayId;

    @Column(name = "holiday_name", nullable = false, length = 100)
    private String holidayName;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "description", length = 300)
    private String description;

    public Holiday() {
    }

    public Holiday(String holidayName, LocalDate holidayDate, String description) {
        this.holidayName = holidayName;
        this.holidayDate = holidayDate;
        this.description = description;
    }


    public Long getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(Long holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
=======
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "holiday_name", nullable = false)
	private String holidayName;

	@Column(name = "holiday_date", nullable = false)
	private LocalDate holidayDate;

	@Column(name = "description")
	private String description;

	public Holiday() {}

	public Holiday(Long id, String holidayName, LocalDate holidayDate, String description) {
		this.id = id;
		this.holidayName = holidayName;
		this.holidayDate = holidayDate;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public LocalDate getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(LocalDate holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
>>>>>>> dev
