package com.revworkforce.model;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "employee_id", nullable = false, unique = true)
	private String employeeId;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 10)
	private String phoneNumber;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false, length = 10)
	private String emergencyContact;

	@Column(nullable = false)
	private String role;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private LocalDate joiningDate;

	@Column(nullable = false)
	private double salary;

	@ManyToOne
	@JoinColumn(name = "department_id", nullable = false)

	private Department department;

	@ManyToOne
	@JoinColumn(name = "designation_id", nullable = false)
	private Designation designation;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	@JsonBackReference(value = "manager")
	private Employee manager;

	@OneToMany(mappedBy = "employee")
	@JsonManagedReference(value = "employee-leaverequest")
	private List<LeaveRequest> leaveRequests;
	
    @OneToMany(mappedBy = "employee")
	@JsonManagedReference(value = "employee-leavebalance")
    private List<LeaveBalance> leaveBalances;

	public Employee() {}

	public Employee(Long id, String employeeId, String firstName, String lastName, String email, String password,
			String phoneNumber, String address, String emergencyContact, String role, String status,
			LocalDate joiningDate, double salary, Department department, Designation designation, Employee manager) {

		this.id = id;
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.emergencyContact = emergencyContact;
		this.role = role;
		this.status = status;
		this.joiningDate = joiningDate;
		this.salary = salary;
		this.department = department;
		this.designation = designation;
		this.manager = manager;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public List<LeaveRequest> getLeaveRequests() {
		return leaveRequests;
	}

	public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}
	
	public List<LeaveBalance> getLeaveBalances() {
		return leaveBalances;
	}

	public void setLeaveBalances(List<LeaveBalance> leaveBalances) {
		this.leaveBalances = leaveBalances;
	}
	
}