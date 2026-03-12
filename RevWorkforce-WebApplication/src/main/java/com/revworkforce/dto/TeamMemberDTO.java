package com.revworkforce.dto;

public class TeamMemberDTO {

    private Long id;
    private String employeeId;
    private String fullName;
    private String email;
    private String role;
    private String department;
    private String designation;

    public TeamMemberDTO() {}

    public TeamMemberDTO(Long id, String employeeId, String fullName,
                         String email, String role,
                         String department, String designation) {
        this.id = id;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.department = department;
        this.designation = designation;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
    
}