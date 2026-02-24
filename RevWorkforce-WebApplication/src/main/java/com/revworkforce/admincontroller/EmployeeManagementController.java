package com.revworkforce.admincontroller;

<<<<<<< HEAD
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.adminservice.EmployeeManagementService;
import com.revworkforce.model.Employee;
import com.revworkforce.repository.EmployeeRepository;


@Controller
@RequestMapping("/admin")
public class EmployeeManagementController {

    private final EmployeeManagementService employeeService;
    private final EmployeeRepository employeeRepository;
    //private final LeaveManagementService leaveService;

    public EmployeeManagementController(EmployeeManagementService employeeService,
                                        EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {

        Employee admin = employeeRepository
                .findByEmail(authentication.getName())
                .orElse(null);

        model.addAttribute("admin", admin);
        model.addAttribute("adminName",
                admin != null ? admin.getFirstName() : "");

        model.addAttribute("totalEmployees",
                employeeService.getEmployeeCount());

        model.addAttribute("activeEmployees",
                employeeService.getActiveEmployeeCount());

        model.addAttribute("inactiveEmployees",
                employeeService.getInactiveEmployeeCount());

        model.addAttribute("page", "dashboard");

        return "admin/employee_managementt";
    }
    
    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {

        Employee admin = employeeRepository
                .findByEmail(authentication.getName())
                .orElse(null);

        model.addAttribute("admin", admin);
        model.addAttribute("page", "profile");

        return "admin/employee_managementt";
    }
    
    @GetMapping("/employees")
    public String employeeList(Model model) {

        model.addAttribute("employees",
                employeeService.getAllEmployees());

        model.addAttribute("page", "employeeList");

        return "admin/employee_managementt";
    }
    
    @GetMapping("/employees/add")
    public String showAddForm(Model model) {

        model.addAttribute("employee", new Employee());
        model.addAttribute("departments",
                employeeService.getAllDepartments());
        model.addAttribute("designations",
                employeeService.getAllDesignations());
        model.addAttribute("managers",
                employeeService.getAllEmployees());

        model.addAttribute("page", "addEmployee");

        return "admin/employee_managementt";
    }

    @PostMapping("/employees/save")
    public String saveEmployee(@ModelAttribute Employee employee,
                               @RequestParam Long departmentId,
                               @RequestParam Long designationId,
                               @RequestParam(required = false) Long managerId) {

        employeeService.createEmployee(
                employee,
                departmentId,
                designationId,
                managerId
        );

        return "redirect:/admin/employees";
    }
    
    @GetMapping("/employees/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {

        model.addAttribute("employee",
                employeeService.getEmployeeById(id));

        model.addAttribute("page", "editEmployee");

        return "admin/employee_managementt";
    }

    @PostMapping("/employees/update/{id}")
    public String updateEmployee(@PathVariable Long id,
                                 @ModelAttribute Employee employee) {

        employeeService.updateEmployee(id, employee);

        return "redirect:/admin/employees";
    }
    
    @GetMapping("/employees/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);
        return "redirect:/admin/employees";
    }
    
    @GetMapping("/employees/activate/{id}")
    public String activateEmployee(@PathVariable Long id) {

        employeeService.activateEmployee(id);
        return "redirect:/admin/employees";
    }

   
    @GetMapping("/employees/deactivate/{id}")
    public String deactivateEmployee(@PathVariable Long id) {

        employeeService.deactivateEmployee(id);
        return "redirect:/admin/employees";
    }

    
=======
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.revworkforce.adminservice.EmployeeManagementService;
import com.revworkforce.dto.ApiResponse;
import com.revworkforce.dto.EmployeeDTO;

@RestController
@RequestMapping("/admin")
public class EmployeeManagementController {
	
	private final EmployeeManagementService employeeManagementService;

	public EmployeeManagementController(EmployeeManagementService employeeManagementService) {
		this.employeeManagementService = employeeManagementService;
	}

	@PostMapping("/add-employee")
	public ResponseEntity<ApiResponse> addEmployee(@Validated @RequestBody EmployeeDTO dto) {
		return employeeManagementService.addEmployee(dto);
	}

	@GetMapping("/all-employees")
	public ResponseEntity<ResponseEntity<ApiResponse>> getAllEmployees() {
		return ResponseEntity.ok(employeeManagementService.getAllEmployees());
	}
	
>>>>>>> dev
}