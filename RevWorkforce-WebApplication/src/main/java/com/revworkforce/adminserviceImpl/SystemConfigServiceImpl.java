package com.revworkforce.adminserviceImpl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminservice.SystemConfigService;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class SystemConfigServiceImpl implements SystemConfigService{
	private final DepartmentRepository departmentRepository;
	private final DesignationRepository designationRepository;
	private final ActivityLogService activityLogService;
	private final HttpServletRequest request;
	
	public SystemConfigServiceImpl(
	        DepartmentRepository departmentRepository,
	        DesignationRepository designationRepository,
	        ActivityLogService activityLogService,
	        HttpServletRequest request) {

	    this.departmentRepository = departmentRepository;
	    this.designationRepository = designationRepository;
	    this.activityLogService = activityLogService;
	    this.request = request;
	}

	@Override
	public Department saveDepartment(Department department) {
		Department saved = departmentRepository.save(department);

	    activityLogService.log(
	            "Created Department",
	            "System Configuration",
	            "Created department: " + saved.getName(),
	            "SUCCESS",
	            request
	    );

	    return saved;
	}

	@Override
	public List<Department> getAllDepartments() {
	    return departmentRepository.findAll();
	}

	@Override
	public Department getDepartmentById(Long id) {
	    return departmentRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Department not found"));
	}

	@Override
	public void deleteDepartment(Long id) {

	    Department department = departmentRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Department not found"));

	    departmentRepository.delete(department);

	    activityLogService.log(
	            "Deleted Department",
	            "System Configuration",
	            "Deleted department: " + department.getName(),
	            "SUCCESS",
	            request
	    );
	}
	
	@Override
	public Designation saveDesignation(Long departmentId, Designation designation) {

	    Department department = departmentRepository.findById(departmentId)
	            .orElseThrow(() -> new RuntimeException("Department not found"));

	    designation.setDepartment(department);

	    Designation saved = designationRepository.save(designation);

	    activityLogService.log(
	            "Created Designation",
	            "System Configuration",
	            "Created designation: " + saved.getTitle() +
	            " under department: " + department.getName(),
	            "SUCCESS",
	            request
	    );

	    return saved;
	}

	@Override
	public List<Designation> getDesignationsByDepartmentId(Long departmentId) {
	    return designationRepository.findByDepartmentId(departmentId);
	}

	@Override
	public void deleteDesignation(Long id) {

	    Designation designation = designationRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Designation not found"));

	    designationRepository.delete(designation);

	    activityLogService.log(
	            "Deleted Designation",
	            "System Configuration",
	            "Deleted designation: " + designation.getTitle(),
	            "SUCCESS",
	            request
	    );
	}
	
	@Override
	public Designation getDesignationById(Long id) {
	    return designationRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Designation not found"));
	}
	@Override
	public long countDepartments() {
	    return departmentRepository.count();
	}

	@Override
	public long countDesignations() {
	    return designationRepository.count();
	}
	
}