package dat.example.ems.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;

import dat.example.ems.model.Department;
import dat.example.ems.model.Employee;
import dat.example.ems.model.Position;

@Mapper
public interface EmployeeMapper {
    
	@Insert("INSERT INTO Employee(Name, Email, Phone, Salary, Username, Password, DepartmentID, Role) " +
	        "VALUES(#{name}, #{email}, #{phone}, #{salary}, #{username}, #{password}, #{departmentID}, #{role})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(Employee employee);

	@Insert("INSERT INTO Employee(Name, Email, Phone, Salary, Username, Password, DepartmentID, Role) " +
	        "VALUES(#{name}, #{email}, #{phone}, #{salary}, #{username}, #{password}, #{departmentID}, #{role})")
	Employee save(Employee employee);
	
	@Insert({
	    "<script>",
	    "INSERT INTO Employee_Position (EmployeeID, PositionID) VALUES ",
	    "<foreach collection='positions' item='position' separator=','>",
	        "(#{employeeId}, #{position.id})",
	    "</foreach>",
	    "</script>"
	})
	void insertEmployeePositions(@Param("employeeId") int employeeId, @Param("positions") List<Position> positions);

    
    @Update("UPDATE Employee SET Name = #{name}, Email = #{email}, Phone = #{phone}, Salary = #{salary}, Username = #{username}, Password = #{password}, DepartmentID = #{departmentID}, Role =#{role} WHERE id = #{id}")
    void update(Employee employee);
    
    @Delete("DELETE FROM Employee WHERE id = #{id}")
    void delete(int id);
    
    @Select("SELECT * FROM Employee WHERE id = #{id}")
    Employee findById(int id);
    
    @Results(id = "employeeResultMap", value = {
    	    @Result(property = "id", column = "id"),
    	    @Result(property = "name", column = "name"),
    	    @Result(property = "email", column = "email"),
    	    @Result(property = "phone", column = "phone"),
    	    @Result(property = "salary", column = "salary"),
    	    @Result(property = "username", column = "username"),
    	    @Result(property = "department", column = "id", javaType = Department.class, one = @One(select = "findDepartmentById")),
    	    @Result(property = "role", column = "role"),
    	    @Result(property = "positions", column = "id", javaType = List.class, many = @Many(select = "findPositionsByEmployeeId"))
    	})
    	@Select("SELECT e.*, d.Name AS DepartmentName " +
    	        "FROM Employee e " +
    	        "INNER JOIN Department d ON e.DepartmentID = d.id ")
    	List<Employee> findAll();

    	@Select("SELECT p.* FROM Employee_Position ep " +
    	        "INNER JOIN Positions p ON ep.PositionID = p.id " +
    	        "WHERE ep.EmployeeID = #{id}")
    	List<Position> findPositionsByEmployeeId(int id);
    	
    	@Select("SELECT name FROM Department WHERE id = #{departmentID}")
    	Department findDepartmentById(int id);

    	@Insert("INSERT INTO Employee_Position (EmployeeID, PositionID) VALUES (#{employeeID}, #{positionID})")
    	void insertEmployeePosition(@Param("employeeID") int employeeID, @Param("positionID") int positionID);

    	@Select("SELECT * FROM Employee WHERE username = #{username}")
        Optional<Employee> findByUsername(@Param("username") String username);
    
}