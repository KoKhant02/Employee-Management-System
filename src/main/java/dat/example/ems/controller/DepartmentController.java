package dat.example.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import dat.example.ems.model.Department;
import dat.example.ems.service.DepartmentService;
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
public class DepartmentController  implements Serializable{

	private static final long serialVersionUID = 1L;


    @Autowired
    private DepartmentService departmentService;
    
    private List<Department> allDepartments;
    private Department department;
    
    public List<Department> getAllDepartments() {
		return allDepartments;
	}

	public void setAllDepartments(List<Department> allDepartments) {
		this.allDepartments = allDepartments;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@PostConstruct
    public void init() {
        department = new Department();
        refreshDepartmentList();
    }

    public void createDepartment() {
        try {
            departmentService.createDepartment(department);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Department created successfully"));
            refreshDepartmentList();
            department = new Department(); // Clear the form after successful creation
        } catch (DataIntegrityViolationException e) {
            handleDuplicateEntryException(e);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to create department"));
        }
    }
    
    private void handleDuplicateEntryException(DataIntegrityViolationException e) {
        String errorMessage = e.getMessage();
        if (errorMessage.contains("Duplicate entry")) {
            if (errorMessage.contains("Code")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Code already exists"));
            } else if (errorMessage.contains("Name")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Department Name already exists"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Constraint violation occurred"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unknown constraint violation"));
        }
    }

    public void editDepartment(int id) {
        Department existingDepartment = departmentService.getDepartmentById(id);
        if (existingDepartment == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Department not found"));
            return;
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editdepartment.xhtml?id=" + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void saveDepartment() {
        departmentService.updateDepartment(department);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteDepartment(int id) {
    	Department existingDepartment = departmentService.getDepartmentById(id);
        if (existingDepartment == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Department not found"));
            return;
        }
        try {
            departmentService.deleteDepartment(id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Department deleted successfully"));
            refreshDepartmentList();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete department"));
        }
    }

    private void refreshDepartmentList() {
        allDepartments = departmentService.getAllDepartments();
    }
    
    public Department getDepartmentById(int id) {
        return departmentService.getDepartmentById(id);
    }
    
}

