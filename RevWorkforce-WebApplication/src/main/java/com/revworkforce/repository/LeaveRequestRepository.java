package com.revworkforce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

	List<LeaveRequest> findByEmployee_Manager_Id(Long managerId);

	List<LeaveRequest> findByEmployee_Id(Long employeeId);

	List<LeaveRequest> findByEmployee_IdOrderByStartDateDesc(Long employeeId);

	long countByEmployee_IdAndLeaveApproval_Status(Long employeeId, String status);

	@Query(value = """
			SELECT COUNT(*)
			FROM leave_requests lr
			JOIN leave_approvals la
			    ON lr.approval_id = la.id
			WHERE lr.employee_id = :employeeId
			AND la.status = :status
			""", nativeQuery = true)
	long countByEmployeeAndStatusNative(@Param("employeeId") Long employeeId, @Param("status") String status);

}