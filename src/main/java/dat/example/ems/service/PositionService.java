package dat.example.ems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dat.example.ems.mapper.PositionMapper;
import dat.example.ems.model.Position;

import java.util.List;

@Service
public class PositionService {

    @Autowired
    private PositionMapper positionMapper;

    public void createPosition(Position position) {
        positionMapper.insert(position);
    }

    public void updatePosition(Position position) {
        positionMapper.update(position);
    }

    public void deletePosition(int id) {
        positionMapper.delete(id);
    }

    public Position getPositionById(int id) {
        return positionMapper.findById(id);
    }

    public List<Position> getAllPositions() {
        return positionMapper.findAll();
    }
}
