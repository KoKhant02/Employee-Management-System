package dat.example.ems.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class MainController {
    
	@GetMapping("/login")
    public String login() {
        return "signin.xhtml?faces-redirect=true";
    }
	
	@GetMapping("/dashboard")
	public String dashboard() {
		return "index.xhtml?faces-redirect=true";
	}
}
