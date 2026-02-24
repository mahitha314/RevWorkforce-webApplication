package com.workforce.adminservice;

import com.workforce.model.Department;
import com.workforce.model.Designation;
import com.workforce.repository.DepartmentRepository;
import com.workforce.repository.DesignationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SystemConfigService {

    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    public SystemConfigService(DepartmentRepository departmentRepository,
                               DesignationRepository designationRepository) {
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
    }

    public Department addDepartment(Department department) {

        if (department.getName() == null || department.getName().isBlank()) {
            throw new RuntimeException("Department name cannot be empty");
        }

        return departmentRepository.save(department);
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    public Department updateDepartment(Long id, Department updated) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Department not found"));

        department.setName(updated.getName());

        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {

        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Department not found");
        }

        departmentRepository.deleteById(id);
    }
    public Designation addDesignation(Designation designation) {

        if (designation.getTitle() == null || designation.getTitle().isBlank()) {
            throw new RuntimeException("Designation title cannot be empty");
        }

        return designationRepository.save(designation);
    }

    public List<Designation> getDesignations() {
        return designationRepository.findAll();
    }

    public Designation updateDesignation(Long id, Designation updated) {

        Designation designation = designationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Designation not found"));

        designation.setTitle(updated.getTitle());

        return designationRepository.save(designation);
    }

    public void deleteDesignation(Long id) {

        if (!designationRepository.existsById(id)) {
            throw new RuntimeException("Designation not found");
        }

        designationRepository.deleteById(id);
    }
}