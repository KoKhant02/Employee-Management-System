package dat.example.ems.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import dat.example.ems.model.Department;

@Mapper
public interface DepartmentMapper {
    
    @Insert("INSERT INTO Department(Code,Name) VALUES(#{code},#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Department department);
    
    @Update("UPDATE Department SET Code = #{code}, Name = #{name} WHERE id = #{id}")
    void update(Department department);
    
    @Delete("DELETE FROM Department WHERE id = #{id}")
    void delete(int id);
    
    @Select("SELECT * FROM Department WHERE id = #{id}")
    Department findById(int id);
    
    @Select("SELECT * FROM Department")
    List<Department> findAll();
    
    @Select("SELECT id, name FROM Department")
    List<Department> getDeptList();
}