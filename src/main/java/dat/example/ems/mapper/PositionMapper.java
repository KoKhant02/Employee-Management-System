package dat.example.ems.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dat.example.ems.model.Position;

@Mapper
public interface PositionMapper {
    
    @Insert("INSERT INTO Positions(name) VALUES(#{name})")
    void insert(Position position);
    
    @Update("UPDATE Positions SET name = #{name} WHERE id = #{id}")
    void update(Position position);
    
    @Delete("DELETE FROM Positions WHERE id = #{id}")
    void delete(int id);
    
    @Select("SELECT * FROM Positions WHERE id = #{id}")
    Position findById(int id);
    
    @Select("SELECT * FROM Positions")
    List<Position> findAll();
    
    @Select("SELECT id, name FROM Positions")
    List<Position> positionList();
}