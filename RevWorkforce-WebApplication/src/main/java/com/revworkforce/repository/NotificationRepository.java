
package com.revworkforce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revworkforce.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{

	List<Notification> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    List<Notification> findByEmployeeIdAndIsRead(Long employeeId, Boolean isRead);
    
    List<Notification> findAllByOrderByCreatedAtDesc();

    long countByEmployeeIdAndIsRead(Long employeeId, Boolean isRead);
	
}
