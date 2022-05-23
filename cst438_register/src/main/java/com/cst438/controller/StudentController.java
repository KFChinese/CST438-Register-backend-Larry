package com.cst438.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

	@Autowired
	StudentRepository studentRepository;
	
	@GetMapping("/student")
	public StudentDTO getStudent(@RequestParam("email") String email) {
		Student student = studentRepository.findByEmail(email);
		
		if(student == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This Student's Email might be typed incorrectly, or not exist, \nEmail: " + email +"\nPlease try again.");
		}
		
		return createStudent(student);
	}
	
	@PostMapping("/student")
	@Transactional
	public StudentDTO addStudent( @RequestBody StudentDTO newStudentDTO) {
		Student student = studentRepository.findByEmail(newStudentDTO.email);
		
		if(student != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This Student's Email already exists. \nEmail: " + newStudentDTO.email + "Please try again.");
		}
		
		student = new Student();
		student.setEmail(newStudentDTO.email);
		student.setName(newStudentDTO.name);
		Student result = studentRepository.save(student);
		return createStudent(result);
	}
	
	@PutMapping("/student/hold/{id}")
	@Transactional
	public void holdStudent(@PathVariable (value = "id") int id) {
		Student student = studentRepository.findById(id);
		
		if(student == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can NOT Place hold Student due to The id:"+ id + " could be possibly incorrect, \nor, this Student does not exist. \nPlease Try again.");
		}
		
		student.setStatus("HOLD");
		student.setStatusCode(1);
		studentRepository.save(student);
	}
	
	@PutMapping("/student/release/{id}")
	public void releaseStudent(@PathVariable (value = "id") int id) {
		Student student = studentRepository.findById(id);
		
		if(student == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can NOT Release hold Student due to The id:\"+ id + \" could be possibly incorrect, \\nor, this Student does not exist. \\nPlease Try again.");
		}
		
		student.setStatus("ACTIVE");
		student.setStatusCode(0);
		studentRepository.save(student);
	}
	
	private StudentDTO createStudent(Student student) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.stud_id = student.getStudent_id();
		studentDTO.name = student.getName();
		studentDTO.email = student.getEmail();
		studentDTO.code = student.getStatusCode();
		studentDTO.status = student.getStatus();
		return studentDTO;
	}
}