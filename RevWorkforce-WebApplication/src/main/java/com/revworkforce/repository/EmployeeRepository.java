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

		List<Employee> findByRoleIgnoreCase(String role);

		List<Employee> findByDesignationId(Long designationId);

		List<Employee> findByStatus(String status);

		List<Employee> findByDepartmentIdAndStatus(Long departmentId, String status);
		boolean existsByEmployeeId(String employeeId);

		List<Employee> findByManagerId(Long managerId);

		long countByDepartmentId(Long departmentId);

		long countByDesignationId(Long designationId);

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