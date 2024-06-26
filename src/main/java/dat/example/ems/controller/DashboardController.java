package dat.example.ems.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public String getDashboard() {
        return "dashboard.xhtml";
    }
}