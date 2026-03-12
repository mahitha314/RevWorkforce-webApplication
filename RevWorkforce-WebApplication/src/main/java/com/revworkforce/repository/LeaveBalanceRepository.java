package com.revworkforce.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.LeaveType;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

	List<LeaveBalance> findByEmployee(Employee employee);
	List<LeaveBalance> findByEmployee_Id(Long employeeId);
    Optional<LeaveBalance> findByEmployeeAndLeaveType(Employee employee, LeaveType leaveType);
	@Query("SELECT lb FROM LeaveBalance lb WHERE lb.employee.department.id = :departmentId")
	List<LeaveBalance> findByDepartmentId(Long departmentId);
	
	Optional<LeaveBalance> findByEmployee_IdAndLeaveType_Id(Long employeeId, Long leaveTypeId);

}