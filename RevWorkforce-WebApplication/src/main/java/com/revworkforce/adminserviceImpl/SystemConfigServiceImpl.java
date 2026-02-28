package com.revworkforce.adminserviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revworkforce.adminservice.SystemConfigService;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
@Service
public class SystemConfigServiceImpl implements SystemConfigService{
	private final DepartmentRepository departmentRepository;
	private final DesignationRepository designationRepository;
	public SystemConfigServiceImpl(DepartmentRepository departmentRepository,
            DesignationRepository designationRepository) {
this.departmentRepository = departmentRepository;
this.designationRepository = designationRepository;
}
	@Override
	public Department saveDepartment(Department department) {
	    return departmentRepository.save(department);
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
	    departmentRepository.deleteById(id);
	}	
	
	@Override
	public Designation saveDesignation(Long departmentId, Designation designation) {

	    Department department = departmentRepository.findById(departmentId)
	            .orElseThrow(() -> new RuntimeException("Department not found"));

	    designation.setDepartment(department);
	    return designationRepository.save(designation);
	}

	@Override
	public List<Designation> getDesignationsByDepartmentId(Long departmentId) {
	    return designationRepository.findByDepartmentId(departmentId);
	}

	@Override
	public void deleteDesignation(Long id) {
	    designationRepository.deleteById(id);
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
