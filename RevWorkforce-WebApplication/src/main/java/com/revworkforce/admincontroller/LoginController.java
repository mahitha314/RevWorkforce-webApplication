package com.revworkforce.admincontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String selectRolePage() {
        return "select-role";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}