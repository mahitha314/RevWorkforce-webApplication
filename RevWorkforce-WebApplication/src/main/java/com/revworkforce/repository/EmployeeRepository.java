package com.revworkforce.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);
	Optional<Employee> findByEmployeeId(String employeeId);
	List<Employee> findByManager_Id(Long managerId);
	 @Query("""
		        SELECT e FROM Employee e
		        WHERE lower(e.firstName) LIKE lower(concat('%', :q, '%'))
		           OR lower(e.lastName) LIKE lower(concat('%', :q, '%'))
		           OR lower(e.email) LIKE lower(concat('%', :q, '%'))
		           OR lower(e.employeeId) LIKE lower(concat('%', :q, '%'))
		           OR lower(e.department.name) LIKE lower(concat('%', :q, '%'))
		           OR lower(e.designation.title) LIKE lower(concat('%', :q, '%'))
		    """)
	 List<Employee> search(@Param("q") String q);
	 List<Employee> findByRole(String role);
	 List<Employee> findByDepartmentId(Long departmentId);
	 long countByRole(String role);
	
	 List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
	            String firstName,
	            String lastName,
	            String email
	    );
	    List<Employee> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
	            String firstName,
	            String lastName
	    );

	    
	    List<Employee> findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
	            String firstName,
	            String email
	    );
		Optional<Employee> findByEmailIgnoreCase(String email);
		
		// ===== GET MANAGERS =====
		List<Employee> findByRoleIgnoreCase(String role);

		// ===== FILTER BY DESIGNATION =====
		List<Employee> findByDesignationId(Long designationId);

		// ===== ACTIVE EMPLOYEES =====
		List<Employee> findByStatus(String status);

		// ===== ACTIVE EMPLOYEES BY DEPARTMENT =====
		List<Employee> findByDepartmentIdAndStatus(Long departmentId, String status);
		boolean existsByEmployeeId(String employeeId);

		// ===== GET EMPLOYEES UNDER A MANAGER =====
		List<Employee> findByManagerId(Long managerId);

		// ===== COUNT EMPLOYEES IN DEPARTMENT =====
		long countByDepartmentId(Long departmentId);

		// ===== COUNT EMPLOYEES BY DESIGNATION =====
		long countByDesignationId(Long designationId);
		 // ===== EMPLOYEES NOT YET ASSIGNED TO SELECTED LEAVE TYPE =====
	    @Query("""
	        SELECT e FROM Employee e
	        WHERE e.id NOT IN (
	            SELECT lb.employee.id
	            FROM LeaveBalance lb
	            WHERE lb.leaveType.id = :leaveTypeId
	        )
	    """)
	    List<Employee> findEmployeesNotAssignedToLeaveType(@Param("leaveTypeId") Long leaveTypeId);
}