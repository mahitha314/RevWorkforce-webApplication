package com.revworkforce.repository;
<<<<<<< HEAD
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revworkforce.model.*;

public interface LeaveBalanceRepository
        extends JpaRepository<LeaveBalance, Long> {

    List<LeaveBalance> findByEmployee(Employee employee);
}
=======

public interface LeaveBalanceRepository {

}
>>>>>>> dev
