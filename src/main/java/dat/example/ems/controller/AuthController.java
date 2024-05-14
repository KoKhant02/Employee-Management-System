package dat.example.ems.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import dat.example.ems.model.ReqRes;
import dat.example.ems.service.AuthService;

@Controller
@ManagedBean(name = "authController")
@SessionScoped
public class AuthController {
	 
  @Autowired 
  private AuthService authService;
		 
  private String username;
  private String password;

  // Getters and setters for username and password properties

  @PostMapping("/signin")
  public String signIn() {
    ReqRes signInRequest = new ReqRes(); // Create signInRequest object
    signInRequest.setUsername(username); // Set username from the form
    signInRequest.setPassword(password); // Set password from the form
    ReqRes response = authService.signIn(signInRequest);
    // Handle response as needed
    return "index"; // Return the appropriate view name
  }
}

/*
 * @PostMapping("/signin") public ResponseEntity<ReqRes> signIn(@RequestBody
 * ReqRes signInRequest) { ReqRes response = authService.signIn(signInRequest);
 * return ResponseEntity.ok(response); }
 */