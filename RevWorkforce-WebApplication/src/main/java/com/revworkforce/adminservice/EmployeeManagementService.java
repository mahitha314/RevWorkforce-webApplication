package com.workforce.adminservice;

import com.workforce.exception.EmployeeNotFoundException;
import com.workforce.model.Department;
import com.workforce.model.Designation;
import com.workforce.model.Employee;
import com.workforce.repository.DepartmentRepository;
import com.workforce.repository.DesignationRepository;
import com.workforce.repository.EmployeeRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;

    public EmployeeManagementService(EmployeeRepository employeeRepository,
                                     DepartmentRepository departmentRepository,
                                     DesignationRepository designationRepository,
                                     BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee createEmployee(Employee employee,
                                   Long departmentId,
                                   Long designationId,
                                   Long managerId) {

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Designation designation = designationRepository.findById(designationId)
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        Employee manager = null;
        if (managerId != null) {
            manager = employeeRepository.findById(managerId)
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
        }

        employee.setDepartment(department);
        employee.setDesignation(designation);
        employee.setManager(manager);
        employee.setJoiningDate(LocalDate.now());
        employee.setActive(true);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {

        Employee existingEmployee = getEmployeeById(id);

        existingEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());
        existingEmployee.setAddress(updatedEmployee.getAddress());
        existingEmployee.setEmergencyContact(updatedEmployee.getEmergencyContact());
        existingEmployee.setSalary(updatedEmployee.getSalary());

        return employeeRepository.save(existingEmployee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with ID: " + id));
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public List<Designation> getAllDesignations() {
        return designationRepository.findAll();
    }

    
    public void deleteEmployee(Long id) {

        Employee employee = getEmployeeById(id);

        // Prevent deleting ADMIN account
        if ("ADMIN".equalsIgnoreCase(employee.getRole())) {
            throw new RuntimeException("Admin account cannot be deleted");
        }

        employeeRepository.delete(employee);
    }

    
    public void activateEmployee(Long id) {
        Employee emp = employeeRepository.findById(id).orElseThrow();
        emp.setActive(true);
        employeeRepository.save(emp);
    }

    public void deactivateEmployee(Long id) {
        Employee emp = employeeRepository.findById(id).orElseThrow();
        emp.setActive(false);
        employeeRepository.save(emp);
    }


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