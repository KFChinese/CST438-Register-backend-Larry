package com.cst438;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = {StudentController.class})
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {
	
	public String STUDENT_EMAIL = "test@csumb.edu";
	public String STUDENT_NAME = "test";
	public int STUDENT_ID = 1;
	public String STUDENT_EP = "/student";
	public String HOLD_EP = "/hold";
	public String RELEASE_EP = "/release";
	private Student student;
	
	@MockBean
	StudentRepository studentRepository;
	
	@Autowired
	private MockMvc mvc;
	
	@BeforeEach
	public void setUp() {
		student = new Student();
		student.setEmail(STUDENT_EMAIL);
		student.setName(STUDENT_NAME);
		student.setStatusCode(0);
		student.setStudent_id(STUDENT_ID);
	}
	
	// Find Student
	
	@Test
	public void FindExistingStudent() throws Exception {
		given(studentRepository.findByEmail(STUDENT_EMAIL)).willReturn(student);
		
		MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
				.get(STUDENT_EP + "?email=" + STUDENT_EMAIL)
				.accept(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();

		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		
		assertEquals(200, response.getStatus());
		assertEquals(STUDENT_ID, result.stud_id);
	}
	
	// Add new Student
	
	@Test
	void AddNewStudent() throws Exception {
		given(studentRepository.findByEmail(STUDENT_EMAIL)).willReturn(null);
		given(studentRepository.save(any(Student.class))).willReturn(student);
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = STUDENT_EMAIL;
		studentDTO.name = STUDENT_NAME;
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.post(STUDENT_EP)
				.content(asJsonString(studentDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		
		MockHttpServletResponse response = mvc
				.perform(builder)
				.andReturn()
				.getResponse();

		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertEquals(200, response.getStatus());
		assertEquals(STUDENT_ID, result.stud_id);
		verify(studentRepository).save(any(Student.class));
	}
	
	// Place Hold on Student
	
	@Test
	void PleaceHoldonStudent() throws Exception {
		given(studentRepository.findById(STUDENT_ID)).willReturn(student);
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put(
				STUDENT_EP + HOLD_EP + "/" + STUDENT_ID);
		mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentRepository, times(1)).findById(STUDENT_ID);
	}

	
	// Release Hold on Student

	@Test
	void ReleaseHoldOnStudent() throws Exception {
		given(studentRepository.findById(STUDENT_ID)).willReturn(student);
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put(
				STUDENT_EP + RELEASE_EP + "/" + STUDENT_ID);
		mvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk());
		
		verify(studentRepository, times(1)).findById(STUDENT_ID);
	}
	


	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}