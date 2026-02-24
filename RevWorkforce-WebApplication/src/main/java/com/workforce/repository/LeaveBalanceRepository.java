package com.workforce.repository;
import java.util.List;
import com.workforce.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveBalanceRepository
        extends JpaRepository<LeaveBalance, Long> {

    List<LeaveBalance> findByEmployee(Employee employee);
}