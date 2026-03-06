package com.revworkforce.admincontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.adminservice.EmployeeManagementService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import com.revworkforce.repository.EmployeeRepository;

@RestController
@RequestMapping("/admin")
public class EmployeeManagementController {

    private final EmployeeManagementService employeeManagementService;
    private final DesignationRepository designationRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeManagementController(
            EmployeeManagementService employeeManagementService,
            DesignationRepository designationRepository,
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository) {

        this.employeeManagementService = employeeManagementService;
        this.designationRepository = designationRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

   

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse> getAllDepartments() {

        List<Department> departments = departmentRepository.findAll();

        return ResponseEntity.ok(
                new ApiResponse(200,
                        "Departments fetched successfully",
                        departments));
    }

    

    @GetMapping("/designations/by-department/{deptId}")
    public ResponseEntity<ApiResponse> getDesignationsByDepartment(
            @PathVariable Long deptId) {

        List<Designation> designations =
                designationRepository.findByDepartmentId(deptId);

        return ResponseEntity.ok(
                new ApiResponse(200,
                        "Designations fetched successfully",
                        designations));
    }

    
    @GetMapping("/managers")
    public ResponseEntity<ApiResponse> getManagers() {

        List<Employee> managers =
                employeeManagementService.getManagers();

        return ResponseEntity.ok(
                new ApiResponse(200,
                        "Managers fetched successfully",
                        managers));
    }

   

    @PostMapping("/employees")   
    public ResponseEntity<ApiResponse> addEmployee(
            @RequestBody EmployeeDTO dto) {

        try {

            Employee employee = new Employee();

            employee.setEmployeeId(dto.getEmployeeId());
            employee.setFirstName(dto.getFirstName());
            employee.setLastName(dto.getLastName());
            employee.setEmail(dto.getEmail());
            employee.setPassword(dto.getPassword()); 
            employee.setRole(dto.getRole());
            employee.setSalary(dto.getSalary());
            employee.setPhoneNumber(dto.getPhoneNumber());
            employee.setEmergencyContact(dto.getEmergencyContact());
            employee.setAddress(dto.getAddress());

            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            Designation designation = designationRepository.findById(dto.getDesignationId())
                    .orElseThrow(() -> new RuntimeException("Designation not found"));

            employee.setDepartment(department);
            employee.setDesignation(designation);

            if (dto.getManagerId() != null) {
                Employee manager = employeeRepository.findById(dto.getManagerId())
                        .orElse(null);
                employee.setManager(manager);
            }

            employeeRepository.save(employee);

            return ResponseEntity.ok(
                    new ApiResponse(
                            200,
                            "Employee added successfully!",
                            employee
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.status(500)
                    .body(new ApiResponse(
                            500,
                            "Employee save failed: " + e.getMessage(),
                            null
                    ));
        }
    }

   

    @GetMapping("/employees")
    public ResponseEntity<ApiResponse> getAllEmployees() {

        return employeeManagementService.getAllEmployees();
    }

    

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<ApiResponse> getEmployeeById(
            @PathVariable String employeeId) {

        return employeeManagementService.getByEmployeeId(employeeId);
    }

    
    @GetMapping("/employees/search")
    public ResponseEntity<ApiResponse> searchEmployees(
            @RequestParam("q") String query) {

        return employeeManagementService.searchEmployees(query);
    }

    @PutMapping("/employees/{employeeId}/deactivate")
    public ResponseEntity<ApiResponse> deactivateEmployee(
            @PathVariable String employeeId,
            @RequestParam(required = false) String reason) {

        return employeeManagementService
                .deactivateEmployee(employeeId, reason);
    }

    @PutMapping("/employees/{employeeId}/reactivate")
    public ResponseEntity<ApiResponse> reactivateEmployee(
            @PathVariable String employeeId,
            @RequestParam(required = false) String reason) {

        return employeeManagementService
                .reactivateEmployee(employeeId, reason);
    }

    @PutMapping("/employees/{employeeId}/manager/{managerId}")
    public ResponseEntity<ApiResponse> changeManager(
            @PathVariable String employeeId,
            @PathVariable Long managerId) {

        return employeeManagementService
                .changeManager(employeeId, managerId);
    }
}