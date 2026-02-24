package com.revworkforce.repository;

<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(Employee employee);

    long countByStatus(String status);
}
=======
public interface LeaveRequestRepository {

}
>>>>>>> dev
