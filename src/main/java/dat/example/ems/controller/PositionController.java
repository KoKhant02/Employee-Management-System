package dat.example.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import dat.example.ems.model.Position;
import dat.example.ems.service.PositionService;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class PositionController  implements Serializable{

	private static final long serialVersionUID = 1L;

    @Autowired
    private PositionService positionService;
    
    private List<Position> allPositions;
    private Position position;
    
	@PostConstruct
    public void init() {
        position = new Position();
        refreshPositionList();
    }
	
    public List<Position> getAllPositions() {
		return allPositions;
	}

	public void setAllPositions(List<Position> allPositions) {
		this.allPositions = allPositions;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void createPosition() {
        try {
            positionService.createPosition(position);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Position created successfully"));
            refreshPositionList();
            position = new Position(); // Clear the form after successful creation
        } catch (DataIntegrityViolationException e) {
            handleDuplicateEntryException(e);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to create position"));
        }
    }
    
    private void handleDuplicateEntryException(DataIntegrityViolationException e) {
        String errorMessage = e.getMessage();
        if (errorMessage.contains("Duplicate entry")) {
            if (errorMessage.contains("name")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Position Name already exists"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Constraint violation occurred"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unknown constraint violation"));
        }
    }

    public void editPosition(int id) {
        Position existingPosition = positionService.getPositionById(id);
        if (existingPosition == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Position not found"));
            return;
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editposition.xhtml?id=" + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void savePosition() {
        positionService.updatePosition(position);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePosition(int id) {
    	Position existingPosition = positionService.getPositionById(id);
        if (existingPosition == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Position not found"));
            return;
        }
        try {
            positionService.deletePosition(id);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Position deleted successfully"));
            refreshPositionList();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete position"));
        }
    }

    private void refreshPositionList() {
        allPositions = positionService.getAllPositions();
    }
    
    public Position getPositionById(int id) {
        return positionService.getPositionById(id);
    }
}
