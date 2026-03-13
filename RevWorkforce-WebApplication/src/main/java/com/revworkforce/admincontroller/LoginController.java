package com.revworkforce.admincontroller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@GetMapping("/login")
    public String loginPage() {
        return "login";   
    }
	 @PostMapping("/store-token")
	    public String storeToken(@RequestParam String token, HttpSession session) {

	        session.setAttribute("JWT_TOKEN", token);

	        return "redirect:/admin/dashboard";
	    }
}
