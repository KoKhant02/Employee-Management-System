package dat.example.ems.config;

import dat.example.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JWTAuthFIlter jwtAuthFIlter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                		request -> request
                		.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // Allow access to static resources
                		.requestMatchers("/javax.faces.resource/**").permitAll()// Allow JSF resources
                        .anyRequest().authenticated()
                        )
                .exceptionHandling(
                        (exceptionHandling) -> exceptionHandling
                        .accessDeniedPage("/accessDenied")
                )
                .formLogin(form -> form
                        .loginPage("/login.xhtml")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard.xhtml")
                        .failureUrl("/login.xhtml?error=true")
                        .permitAll()
                ).logout(
                        logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login.xhtml")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .sessionManagement(
                		manager -> manager
        				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFIlter, UsernamePasswordAuthenticationFilter.class
);
        return http.build();
    }
    

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(employeeService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
