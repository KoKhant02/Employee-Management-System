package dat.example.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dat.example.ems.model.Department;
import dat.example.ems.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody Department department) {
    	try {
    		departmentService.createDepartment(department);
            return new ResponseEntity<>("Department created successfully", HttpStatus.CREATED);
    	} catch (DataIntegrityViolationException e) {
    		String errorMessage = e.getMessage();
    		if(errorMessage.contains("Duplicate entry")) {
    			if (errorMessage.contains("Code")) {
    				return new ResponseEntity<>("Code already exists", HttpStatus.CONFLICT);
    			}else if(errorMessage.contains("Name")) {
    				return new ResponseEntity<>("Department Name already exists", HttpStatus.CONFLICT);
    			} else {
                    return new ResponseEntity<>("Constraint violation occurred", HttpStatus.CONFLICT);
                }
    		} else {
                return new ResponseEntity<>("Unknown constraint violation", HttpStatus.CONFLICT);
            }
    	}
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartment(@PathVariable int id, @RequestBody Department department) {
        Department existingDepartment = departmentService.getDepartmentById(id);
        if (existingDepartment == null) {
            return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
        }
        department.setId(id);
        departmentService.updateDepartment(department);
        return new ResponseEntity<>("Department updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable int id) {
        Department existingDepartment = departmentService.getDepartmentById(id);
        if (existingDepartment == null) {
            return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
        }
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>("Department deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }
}

