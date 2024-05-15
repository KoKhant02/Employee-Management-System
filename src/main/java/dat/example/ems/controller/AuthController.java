package dat.example.ems.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import dat.example.ems.model.Employee;
import dat.example.ems.model.ReqRes;
import dat.example.ems.service.AuthService;
import jakarta.annotation.PostConstruct;

@Controller
@ManagedBean(name = "authController")
@SessionScoped
public class AuthController implements Serializable{

  private static final long serialVersionUID = 1L;

  @Autowired 
  private AuthService authService;
		 
  private Employee employee;
  private String responseMessage;

  	@PostConstruct
  	public void init() {
  		employee = new Employee();
  	}
  
  	public Employee getEmployee() {
  		return employee;
	}

  	public void setEmployee(Employee employee) {
  		this.employee = employee;
  	}
  	
	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String signIn() {
        ReqRes signInRequest = new ReqRes();
        signInRequest.setUsername(employee.getUsername());
        signInRequest.setPassword(employee.getPassword());
        ReqRes response = authService.signIn(signInRequest);
        if (response.isSuccess()) {
            responseMessage = "Sign in successful"; // Or any success message
            return "dashboard.xhtml"; // Redirect to the dashboard
        } else {
            responseMessage = "Sign in failed. Please try again."; 
            return null; // Stay on the same page
        }
    }





}

/*
 * @PostMapping("/signin") public ResponseEntity<ReqRes> signIn(@RequestBody
 * ReqRes signInRequest) { ReqRes response = authService.signIn(signInRequest);
 * return ResponseEntity.ok(response); }
 */