package dat.example.ems.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dat.example.ems.model.ReqRes;
import dat.example.ems.service.AuthService;

@Component
public class SignInBean {
    
    @Autowired
    private AuthService authService;
    
    private String username;
    private String password;

    // Getters and setters for username and password

    public String signIn() {
        ReqRes signInRequest = new ReqRes();
        signInRequest.setUsername(username);
        signInRequest.setPassword(password);

        // Call the AuthController's signIn method
        ReqRes responseEntity = authService.signIn(signInRequest);
        
        // Handle the response
        ReqRes response = responseEntity.getBody();
        if (response.getStatusCode() == 200) {
            // Redirect to dashboard or another page on successful sign-in
            return "dashboard?faces-redirect=true";
        } else {
            // Display error message on unsuccessful sign-in
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", response.getError()));
            return null; // Stay on the same page
        }
    }
}

