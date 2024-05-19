package dat.example.ems.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Department {
	private int id;
	private String code;
    private String name;
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @JsonIgnore
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
