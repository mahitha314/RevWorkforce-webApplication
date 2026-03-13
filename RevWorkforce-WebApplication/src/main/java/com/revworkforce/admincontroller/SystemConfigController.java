package com.revworkforce.admincontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revworkforce.adminservice.SystemConfigService;
import com.revworkforce.model.Department;
import com.revworkforce.model.Designation;

@RestController
@RequestMapping("/api/system-config")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    public SystemConfigController(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department saved = systemConfigService.saveDepartment(department);
        return ResponseEntity.ok(saved);
    }

   
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(systemConfigService.getAllDepartments());
    }

   
    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(systemConfigService.getDepartmentById(id));
    }

    
    @DeleteMapping("/departments/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        systemConfigService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully");
    }

   
    @GetMapping("/departments/count")
    public ResponseEntity<Long> countDepartments() {
        return ResponseEntity.ok(systemConfigService.countDepartments());
    }


    
    @PostMapping("/departments/{departmentId}/designations")
    public ResponseEntity<Designation> createDesignation(
            @PathVariable Long departmentId,
            @RequestBody Designation designation) {

        Designation saved = systemConfigService.saveDesignation(departmentId, designation);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/departments/{departmentId}/designations")
    public ResponseEntity<List<Designation>> getDesignationsByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.ok(systemConfigService.getDesignationsByDepartmentId(departmentId));
    }

   
    @GetMapping("/designations/{id}")
    public ResponseEntity<Designation> getDesignationById(@PathVariable Long id) {
        return ResponseEntity.ok(systemConfigService.getDesignationById(id));
    }

   
    @DeleteMapping("/designations/{id}")
    public ResponseEntity<String> deleteDesignation(@PathVariable Long id) {
        systemConfigService.deleteDesignation(id);
        return ResponseEntity.ok("Designation deleted successfully");
    }

    
    @GetMapping("/designations/count")
    public ResponseEntity<Long> countDesignations() {
        return ResponseEntity.ok(systemConfigService.countDesignations());
    }
}