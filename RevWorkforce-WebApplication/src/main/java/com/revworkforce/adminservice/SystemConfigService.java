package com.revworkforce.adminservice;

import java.util.List;

import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;

public interface SystemConfigService {

    // ================= DEPARTMENT =================

    Department saveDepartment(Department department);

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    void deleteDepartment(Long id);

    // ================= DESIGNATION =================

    Designation saveDesignation(Long departmentId, Designation designation);

    List<Designation> getDesignationsByDepartmentId(Long departmentId);

    Designation getDesignationById(Long id);   // ✅ ADD THIS

    void deleteDesignation(Long id);
    long countDepartments();
    long countDesignations();
}