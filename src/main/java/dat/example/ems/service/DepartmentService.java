package dat.example.ems.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dat.example.ems.mapper.DepartmentMapper;
import dat.example.ems.model.Department;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    public void createDepartment(Department department) {
        departmentMapper.insert(department);
    }

    public void updateDepartment(Department department) {
        departmentMapper.update(department);
    }

    public void deleteDepartment(int id) {
        departmentMapper.delete(id);
    }

    public Department getDepartmentById(int id) {
        return departmentMapper.findById(id);
    }

    public List<Department> getAllDepartments() {
        return departmentMapper.findAll();
    }
}
