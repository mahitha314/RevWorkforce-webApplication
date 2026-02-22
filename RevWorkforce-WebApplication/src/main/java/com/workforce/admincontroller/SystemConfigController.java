package com.workforce.admincontroller;

import com.workforce.adminservice.SystemConfigService;
import com.workforce.model.Department;
import com.workforce.model.Designation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/system-config")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    public SystemConfigController(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }
    @GetMapping
    public String showConfigPage(Model model) {

        model.addAttribute("departments", systemConfigService.getDepartments());
        model.addAttribute("designations", systemConfigService.getDesignations());

        model.addAttribute("department", new Department());
        model.addAttribute("designation", new Designation());

        return "admin/system_config";  // templates/admin/system_config.html
    }

    @PostMapping("/department/add")
    public String addDepartment(@ModelAttribute Department department) {
        systemConfigService.addDepartment(department);
        return "redirect:/admin/system-config";
    }

    @PostMapping("/department/update/{id}")
    public String updateDepartment(@PathVariable Long id,
                                   @ModelAttribute Department department) {
        systemConfigService.updateDepartment(id, department);
        return "redirect:/admin/system-config";
    }

    @GetMapping("/department/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        systemConfigService.deleteDepartment(id);
        return "redirect:/admin/system-config";
    }

    @PostMapping("/designation/add")
    public String addDesignation(@ModelAttribute Designation designation) {
        systemConfigService.addDesignation(designation);
        return "redirect:/admin/system-config";
    }

    @PostMapping("/designation/update/{id}")
    public String updateDesignation(@PathVariable Long id,
                                    @ModelAttribute Designation designation) {
        systemConfigService.updateDesignation(id, designation);
        return "redirect:/admin/system-config";
    }

    @GetMapping("/designation/delete/{id}")
    public String deleteDesignation(@PathVariable Long id) {
        systemConfigService.deleteDesignation(id);
        return "redirect:/admin/system-config";
    }
}