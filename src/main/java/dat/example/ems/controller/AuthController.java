package dat.example.ems.controller;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import dat.example.ems.model.Employee;
import dat.example.ems.model.ReqRes;
import dat.example.ems.service.AuthService;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;

import javax.annotation.PostConstruct;

@Named(value = "authController")
@SessionScoped
public class AuthController implements Serializable {

	@Autowired
	private AuthService authService;

	private Employee employee;
	private String token;
	private String refreshToken;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
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
			token = response.getToken();
			refreshToken = response.getRefreshToken();
			FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("token", token, null);
			FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("refreshToken", refreshToken,
					null);
			System.out.println("token is " + token);
			System.out.println("SignIn Succeed");
			return "success"; // Redirect to the dashboard page
		} else {
			responseMessage = "Sign in failed. Please try again.";
			return null; // Stay on the same page
		}
	}

}
