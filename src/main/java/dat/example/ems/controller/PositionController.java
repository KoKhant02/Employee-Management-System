package dat.example.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dat.example.ems.model.Position;
import dat.example.ems.service.PositionService;

import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @PostMapping
    public ResponseEntity<String> createPosition(@RequestBody Position position) {
    	try {
	        positionService.createPosition(position);
	        return new ResponseEntity<>("Position created successfully", HttpStatus.CREATED);
    	} catch (DataIntegrityViolationException e) {
    		String errorMessage = e.getMessage();
    		if(errorMessage.contains("Duplicate entry")) {
    			if(errorMessage.contains("Name")) {
    				return new ResponseEntity<>("Position Name already exists", HttpStatus.CONFLICT);
    			} else {
                    return new ResponseEntity<>("Constraint violation occurred", HttpStatus.CONFLICT);
                }
    		} else {
                return new ResponseEntity<>("Unknown constraint violation", HttpStatus.CONFLICT);
            }
    	}
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePosition(@PathVariable int id, @RequestBody Position position) {
        Position existingPosition = positionService.getPositionById(id);
        if (existingPosition == null) {
            return new ResponseEntity<>("Position not found", HttpStatus.NOT_FOUND);
        }
        position.setId(id);
        positionService.updatePosition(position);
        return new ResponseEntity<>("Position updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePosition(@PathVariable int id) {
        Position existingPosition = positionService.getPositionById(id);
        if (existingPosition == null) {
            return new ResponseEntity<>("Position not found", HttpStatus.NOT_FOUND);
        }
        positionService.deletePosition(id);
        return new ResponseEntity<>("Position deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable int id) {
        Position position = positionService.getPositionById(id);
        if (position == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(position, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Position>> getAllPositions() {
        List<Position> positions = positionService.getAllPositions();
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }
}
