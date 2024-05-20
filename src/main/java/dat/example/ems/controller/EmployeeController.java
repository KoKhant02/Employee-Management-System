package dat.example.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import dat.example.ems.model.Department;
import dat.example.ems.model.Employee;
import dat.example.ems.model.Position;
import dat.example.ems.service.DepartmentService;
import dat.example.ems.service.EmployeeService;
import dat.example.ems.service.PositionService;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;

@Named
@SessionScoped
public class EmployeeController  implements Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
    private EmployeeService employeeService;
	@Autowired
    private DepartmentService departmentService;
	@Autowired
    private PositionService positionService;

    private List<Employee> allEmployees;
    private Employee employee;
    private List<Department> deptList;
    private Department department;
    private List<Position> positionList;
    private Position position;

    @PostConstruct
    public void init() {
        employee = new Employee();
        refreshEmployeeList();
        deptList = departmentService.getDeptList();
        positionList = positionService.getPositionList();
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


	public List<Department> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<Department> deptList) {
		this.deptList = deptList;
	}
	
	public Department getDepartment() {
		return department;
	}
	
	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<Position> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Position> positionList) {
		this.positionList = positionList;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
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
    
	public String createEmployee() {
		employeeService.createEmployee(employee);
        try {
        	System.out.println("Employee Added");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Employee created successfully"));
            refreshEmployeeList();
            employee = new Employee(); // Clear the form after successful creation
            return "dashboard.xhtml?faces-redirect=true";
        } catch (DataIntegrityViolationException e) {
        	System.out.println("Employee Data Duplicating!");
            handleDuplicateEntryException(e);
            
        } catch (Exception e) {
        	System.out.println("Failed to add Employee!");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to create employee"));
            
        }
        return null;
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

    public List<SelectItem> getDeptNames() {
        List<SelectItem> deptNames = new ArrayList<>();
        for (Department department : deptList) {
            deptNames.add(new SelectItem(department.getId(), department.getName()));
        }
        return deptNames;
    }

    public List<SelectItem> getPositionNames() {
        List<SelectItem> positionNames = new ArrayList<>();
        for (Position position : positionList) {
        	positionNames.add(new SelectItem(position.getId(), position.getName()));
        }
        return positionNames;
    }

    public String getPositionNames(Employee employee) {
        List<String> positionNames = new ArrayList<>();
        for (Position position : employee.getPositions()) {
            positionNames.add(position.getName());
        }
        return String.join(", ", positionNames);
    }


}

