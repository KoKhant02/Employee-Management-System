package dat.example.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import dat.example.ems.model.Employee;
import dat.example.ems.service.EmployeeService;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
public class EmployeeController  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
    private EmployeeService employeeService;

    private List<Employee> allEmployees;
    private Employee employee;

    @PostConstruct
    public void init() {
        employee = new Employee();
        refreshEmployeeList();
    }

    public List<Employee> getAllEmployees() {
		return allEmployees;
	}

	public void setAllEmployees(List<Employee> allEmployees) {
		this.allEmployees = allEmployees;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void createEmployee() {
        try {
            employeeService.createEmployee(employee);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Employee created successfully"));
            refreshEmployeeList();
            employee = new Employee(); // Clear the form after successful creation
        } catch (DataIntegrityViolationException e) {
            handleDuplicateEntryException(e);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to create employee"));
        }
    }
    
    private void handleDuplicateEntryException(DataIntegrityViolationException e) {
        String errorMessage = e.getMessage();
        if (errorMessage.contains("Duplicate entry")) {
            if (errorMessage.contains("Username")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username already exists"));
            } else if (errorMessage.contains("Email")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email already exists"));
            } else if (errorMessage.contains("Password")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password already exists"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Constraint violation occurred"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unknown constraint violation"));
        }
    }

    public void editEmployee(int id) {
        Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Employee not found"));
            return;
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editemployee.xhtml?id=" + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveEmployee() {
        employeeService.updateEmployee(employee);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(int id) {
    	Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Employee not found"));
            return;
        }
        try {
            employeeService.deleteEmployee(id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Employee deleted successfully"));
            refreshEmployeeList();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete employee"));
        }
    }

    private void refreshEmployeeList() {
        allEmployees = employeeService.getAllEmployees();
    }
    
    public Employee getEmployeeById(int id) {
        return employeeService.getEmployeeById(id);
    }

    
}

