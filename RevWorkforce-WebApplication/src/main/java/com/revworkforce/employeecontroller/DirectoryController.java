package com.revworkforce.employeecontroller;

import com.revworkforce.dto.ApiResponse;
import com.revworkforce.employeeservice.DirectoryService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
@RequestMapping("/api/employee")
public class DirectoryController {

    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("/directory")
    public ResponseEntity<ApiResponse> getAllEmployees() {

        ApiResponse response = directoryService.getAllEmployees();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/directory/search")
    public ResponseEntity<ApiResponse> searchEmployees(
            @RequestParam String keyword) {

        ApiResponse response = directoryService.searchEmployees(keyword);

        return ResponseEntity.ok(response);
    }
}
