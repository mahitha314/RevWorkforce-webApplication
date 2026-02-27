package com.revworkforce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // Login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Manager dashboard
    @GetMapping("/manager/dashboard")
    public String dashboard() {
        return "manager/dashboard";
    }

    // Team structure page
    @GetMapping("/manager/team_structure")
    public String teamStructure() {
        return "manager/team_structure";
    }

    // Performance review page
    @GetMapping("/manager/performance_review")
    public String performanceReview() {
        return "manager/performance_review";
    }

}