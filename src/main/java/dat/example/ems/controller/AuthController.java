package dat.example.ems.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;

	@Autowired
	private AuthenticationManager authenticationManager;

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * @PostMapping("/signin") public ResponseEntity<ReqRes> signIn(@RequestBody
	 * ReqRes signInRequest){ return
	 * ResponseEntity.ok(authService.signIn(signInRequest)); }
	 * 
	 */
    public String signIn(@RequestParam("username") String username, 
			            @RequestParam("password") String password,
			            Model model) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authenticatedUser = authenticationManager.authenticate(authentication);

            if (authenticatedUser.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
                return "redirect:/dashboard"; 
            }
        } catch (AuthenticationException e) {
            return "redirect:/login?error=true"; 
        }
        return "redirect:/login?error=true";
    }
	
}
