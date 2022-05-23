package com.cst438.domain;

public class StudentDTO {
	public int stud_id;
	public String name;
	public String email;
	public String status;
	public int code;
	
	public StudentDTO() {
		this.stud_id = 0;
		this.name = null;
		this.status = null;
		this.code = 0;
	}
	
	public StudentDTO(String name, String email) {
		this.stud_id = 0;
		this.name = name;
		this.email = email;
		this.status = null;
		this.code = 0;
	}
	
	public StudentDTO(String name, String email, String status, int code) {
		this.stud_id = 0;
		this.name = name;
		this.email = email;
		this.status = status;
		this.code = code;
	}

	@Override
	public String toString() {
		return "StudentDTO [name=" + name + ", email=" + email + ", status="
				+ status + ", code=" + code + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		StudentDTO other = (StudentDTO) obj;
		if (stud_id != other.stud_id)
			return false;

		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		
		if (code != other.code)
			return false;

		return true;
	}
	
	
}
