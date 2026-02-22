package com.workforce.adminservice;

import com.workforce.exception.EmployeeNotFoundException;
import com.workforce.model.Department;
import com.workforce.model.Designation;
import com.workforce.model.Employee;
import com.workforce.repository.DepartmentRepository;
import com.workforce.repository.DesignationRepository;
import com.workforce.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EmployeeManagementService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    public EmployeeManagementService(EmployeeRepository employeeRepository,
                                     DepartmentRepository departmentRepository,
                                     DesignationRepository designationRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
    }

    // ================= CREATE =================
    public Employee createEmployee(Employee employee,
                                   Long departmentId,
                                   Long designationId,
                                   Long managerId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        employee.setDepartment(department);
        employee.setDesignation(designation);
        employee.setJoiningDate(LocalDate.now());
        employee.setActive(true);

        return employeeRepository.save(employee);
    }

    // ================= GET ALL =================
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found"));
    }

    // ================= DELETE =================
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // ================= ACTIVATE =================
    public void activateEmployee(Long id) {
    	employeeRepository.activateEmployeeById(id);
    }

    // ================= DEACTIVATE =================
    public void deactivateEmployee(Long id) {
    	employeeRepository.deactivateEmployeeById(id);
    }

    // ================= DASHBOARD COUNTS =================
    public long getEmployeeCount() {
        return employeeRepository.count();
    }

    public long getActiveEmployeeCount() {
        return employeeRepository.countByActiveTrue();
    }

    public long getInactiveEmployeeCount() {
        return employeeRepository.countByActiveFalse();
    }
}