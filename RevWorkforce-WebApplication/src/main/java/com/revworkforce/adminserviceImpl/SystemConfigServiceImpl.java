package com.revworkforce.adminserviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger =
            LoggerFactory.getLogger(SystemConfigServiceImpl.class);

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

        logger.info("SystemConfigServiceImpl initialized");
    }

    @Override
    public Department saveDepartment(Department department) {

        logger.info("Creating department: {}", department.getName());

        Department saved = departmentRepository.save(department);

        logger.debug("Department saved with ID: {}", saved.getId());

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

        logger.info("Fetching all departments");

        List<Department> departments = departmentRepository.findAll();

        logger.debug("Total departments fetched: {}", departments.size());

        return departments;
    }

    @Override
    public Department getDepartmentById(Long id) {

        logger.info("Fetching department with ID: {}", id);

        return departmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Department not found with ID: {}", id);
                    return new RuntimeException("Department not found");
                });
    }

    @Override
    public void deleteDepartment(Long id) {

        logger.warn("Deleting department with ID: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Department not found with ID: {}", id);
                    return new RuntimeException("Department not found");
                });

        departmentRepository.delete(department);

        logger.info("Department deleted successfully: {}", department.getName());

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

        logger.info("Creating designation: {} for departmentId {}", designation.getTitle(), departmentId);

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> {
                    logger.error("Department not found with ID: {}", departmentId);
                    return new RuntimeException("Department not found");
                });

        designation.setDepartment(department);

        Designation saved = designationRepository.save(designation);

        logger.debug("Designation saved with ID: {}", saved.getId());

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

        logger.info("Fetching designations for departmentId {}", departmentId);

        List<Designation> designations =
                designationRepository.findByDepartmentId(departmentId);

        logger.debug("Designations fetched: {}", designations.size());

        return designations;
    }

    @Override
    public void deleteDesignation(Long id) {

        logger.warn("Deleting designation with ID: {}", id);

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Designation not found with ID: {}", id);
                    return new RuntimeException("Designation not found");
                });

        designationRepository.delete(designation);

        logger.info("Designation deleted successfully: {}", designation.getTitle());

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

        logger.info("Fetching designation with ID: {}", id);

        return designationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Designation not found with ID: {}", id);
                    return new RuntimeException("Designation not found");
                });
    }

    @Override
    public long countDepartments() {

        logger.info("Counting departments");

        return departmentRepository.count();
    }

    @Override
    public long countDesignations() {

        logger.info("Counting designations");

        return designationRepository.count();
    }
}