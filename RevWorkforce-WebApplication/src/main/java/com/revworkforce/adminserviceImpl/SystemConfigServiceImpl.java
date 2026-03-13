package com.revworkforce.adminserviceImpl;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.revworkforce.adminservice.ActivityLogService;
import com.revworkforce.adminservice.SystemConfigService;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class SystemConfigServiceImpl implements SystemConfigService {

	private static final Logger logger = LogManager.getLogger(SystemConfigServiceImpl.class);

	private final DepartmentRepository departmentRepository;
	private final DesignationRepository designationRepository;
	private final ActivityLogService activityLogService;
	private final HttpServletRequest request;

	public SystemConfigServiceImpl(DepartmentRepository departmentRepository,
			DesignationRepository designationRepository, ActivityLogService activityLogService,
			HttpServletRequest request) {

		this.departmentRepository = departmentRepository;
		this.designationRepository = designationRepository;
		this.activityLogService = activityLogService;
		this.request = request;
	}

	@Override
	public Department saveDepartment(Department department) {

		logger.info("Creating department {}", department.getName());

		Department saved = departmentRepository.save(department);

		logger.info("Department created successfully {}", saved.getName());

		activityLogService.log("Created Department", "System Configuration", "Created department: " + saved.getName(),
				"SUCCESS", request);

		return saved;
	}

	@Override
	public List<Department> getAllDepartments() {

		logger.info("Fetching all departments");

		List<Department> departments = departmentRepository.findAll();

		logger.debug("Total departments fetched {}", departments.size());

		return departments;
	}

	@Override
	public Department getDepartmentById(Long id) {

		logger.info("Fetching department with id {}", id);

		return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
	}

	@Override
	public void deleteDepartment(Long id) {

		logger.info("Deleting department with id {}", id);

		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Department not found"));

		departmentRepository.delete(department);

		logger.info("Department deleted successfully {}", department.getName());

		activityLogService.log("Deleted Department", "System Configuration",
				"Deleted department: " + department.getName(), "SUCCESS", request);
	}

	@Override
	public Designation saveDesignation(Long departmentId, Designation designation) {

		logger.info("Creating designation {} for department {}", designation.getTitle(), departmentId);

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new RuntimeException("Department not found"));

		designation.setDepartment(department);

		Designation saved = designationRepository.save(designation);

		logger.info("Designation created successfully {}", saved.getTitle());

		activityLogService.log("Created Designation", "System Configuration",
				"Created designation: " + saved.getTitle() + " under department: " + department.getName(), "SUCCESS",
				request);

		return saved;
	}

	@Override
	public List<Designation> getDesignationsByDepartmentId(Long departmentId) {

		logger.info("Fetching designations for department {}", departmentId);

		return designationRepository.findByDepartmentId(departmentId);
	}

	@Override
	public void deleteDesignation(Long id) {

		logger.info("Deleting designation with id {}", id);

		Designation designation = designationRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Designation not found"));

		designationRepository.delete(designation);

		logger.info("Designation deleted successfully {}", designation.getTitle());

		activityLogService.log("Deleted Designation", "System Configuration",
				"Deleted designation: " + designation.getTitle(), "SUCCESS", request);
	}

	@Override
	public Designation getDesignationById(Long id) {

		logger.info("Fetching designation with id {}", id);

		return designationRepository.findById(id).orElseThrow(() -> new RuntimeException("Designation not found"));
	}

	@Override
	public long countDepartments() {

		logger.debug("Counting departments");

		return departmentRepository.count();
	}

	@Override
	public long countDesignations() {

		logger.debug("Counting designations");

		return designationRepository.count();
	}

}