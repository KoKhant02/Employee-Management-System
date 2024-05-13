package dat.example.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dat.example.ems.mapper.EmployeeMapper;
import dat.example.ems.model.Employee;

@Service
public class EmployeeService implements UserDetailsService{

    @Autowired
    private EmployeeMapper employeeMapper;

    public void createEmployee(Employee employee) {
        employeeMapper.insert(employee); 
        
        if (employee.getPositions() != null && !employee.getPositions().isEmpty()) {
            employeeMapper.insertEmployeePositions(employee.getId(), employee.getPositions());
        }
    }

    public void updateEmployee(Employee employee) {
        employeeMapper.update(employee);
    }

    public void deleteEmployee(int id) {
        employeeMapper.delete(id);
    }

    public Employee getEmployeeById(int id) {
        return employeeMapper.findById(id);
    }

    public List<Employee> getAllEmployees() {
        return employeeMapper.findAll();
    }
    
    public void addPositionToEmployee(int employeeID, int positionID) {
        employeeMapper.insertEmployeePosition(employeeID, positionID);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeeMapper.findByUsername(username)
        		.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    
}
