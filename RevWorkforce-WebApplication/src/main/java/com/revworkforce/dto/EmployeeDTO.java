
package com.revworkforce.dto;

import jakarta.validation.constraints.*;

public class EmployeeDTO {

	@NotBlank
    private String employeeId;

	@NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

	@NotBlank(message = "Password required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{6,}$",
            message = "Password must contain 1 uppercase, 1 number, 1 special char and min 6 characters"
    )
    private String password;
	
	@NotBlank
	private String role; 

    @NotNull
    private Long departmentId;

    @NotNull
    private Long designationId;

	private Long managerId; 

	@NotNull(message = "Salary required")
    @Positive(message = "Salary must be positive")
	private Double salary;

	@NotBlank(message = "Phone number required")

    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank
    private String address;

    @NotBlank(message = "Emergency contact required")
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Emergency contact must be 10 digits")
    private String emergencyContact;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

}