package dat.example.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dat.example.ems.model.Employee;
import dat.example.ems.service.EmployeeService;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@RestController
@RequestMapping("/employees")
@ManagedBean
@SessionScoped
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
    	try {
    		employeeService.createEmployee(employee);
            return new ResponseEntity<>("Employee created successfully", HttpStatus.CREATED);
    	} catch (DataIntegrityViolationException e) {
    		String errorMessage = e.getMessage();
    		if (errorMessage.contains("Duplicate entry")) {
                if (errorMessage.contains("Username")) {
                    return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
                } else if (errorMessage.contains("Email")) {
                    return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
                } else if (errorMessage.contains("Password")) {
                    return new ResponseEntity<>("Password already exists", HttpStatus.CONFLICT);
                } else {
                    return new ResponseEntity<>("Constraint violation occurred", HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>("Unknown constraint violation", HttpStatus.CONFLICT);
            }
    	}
        
        
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee == null) {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
        employee.setId(id);
        employeeService.updateEmployee(employee);
        return new ResponseEntity<>("Employee updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id) {
        Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee == null) {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    
}

