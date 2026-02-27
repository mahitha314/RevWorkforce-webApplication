
package com.revworkforce.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveType;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

	List<LeaveBalance> findByEmployeeId(Long employeeId);
	
	List<LeaveBalance> findByEmployee(Employee employee);

    Optional<LeaveBalance> findByEmployeeAndLeaveType(Employee employee, LeaveType leaveType);
    
	@Query("SELECT lb FROM LeaveBalance lb WHERE lb.employee.department.id = :departmentId")
	List<LeaveBalance> findByDepartmentId(Long departmentId);
}
