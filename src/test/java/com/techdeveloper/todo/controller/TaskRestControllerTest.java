package com.techdeveloper.todo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techdeveloper.todo.dto.ApiResponseDto;
import com.techdeveloper.todo.dto.TaskDto;
import com.techdeveloper.todo.entity.Task;
import com.techdeveloper.todo.exception.GlobalExceptionHandler;
import com.techdeveloper.todo.form.TaskForm;
import com.techdeveloper.todo.service.TaskServices;

class TaskRestControllerTest {

	MockMvc mockMvc;

	TaskServices taskServices;

	@BeforeEach
	void init() {
		taskServices = mock(TaskServices.class);
		TaskRestController taskRestController = new TaskRestController(taskServices);
		GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
		mockMvc = MockMvcBuilders.standaloneSetup(taskRestController).setControllerAdvice(globalExceptionHandler)
				.build();
	}

	@Test
	void testGetTaskListSuccess() throws URISyntaxException, Exception {
		ApiResponseDto<List<TaskDto>> apiResponseDto = new ApiResponseDto<>(Collections.emptyList(),
				"Task list fetched successfully.", true, HttpStatus.OK.value(), System.currentTimeMillis());
		when(taskServices.getTaskList()).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(new URI("/api/v1/tasks")).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(200, response.getStatus());
		verify(taskServices).getTaskList();
	}

	@Test
	void testGetTaskSuccess() throws URISyntaxException, Exception {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Title 1");
		task.setDescription("Description 1");
		task.setCreatedAt(LocalDateTime.now());
		task.setUpdatedAt(LocalDateTime.now());
		TaskDto taskDto = new TaskDto(task);
		ApiResponseDto<TaskDto> apiResponseDto = new ApiResponseDto<>(taskDto, "Task found successfully.", true,
				HttpStatus.OK.value(), System.currentTimeMillis());
		when(taskServices.getTask(anyLong())).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(
						MockMvcRequestBuilders.get(new URI("/api/v1/tasks/1")).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(200, response.getStatus());
		verify(taskServices).getTask(anyLong());
	}

	@Test
	void testDeleteTaskSuccess() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Task deleted successfully.", true,
				HttpStatus.OK.value(), System.currentTimeMillis());
		when(taskServices.deleteTask(anyLong())).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.delete(new URI("/api/v1/tasks/1")).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(200, response.getStatus());
		verify(taskServices).deleteTask(anyLong());
	}

	@Test
	void testAddTaskSuccess() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Task created successfully.", true,
				HttpStatus.CREATED.value(), System.currentTimeMillis());
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription("Description 1");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.addTask(taskForm)).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post(new URI("/api/v1/tasks")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(201, response.getStatus());
		verify(taskServices).addTask(taskForm);
	}

	@Test
	void testUpdateTaskSuccess() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Task updated successfully.", true,
				HttpStatus.OK.value(), System.currentTimeMillis());
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Updated Title 1");
		taskForm.setDescription("Updated Description 1");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.updateTask(1L, taskForm)).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.put(new URI("/api/v1/tasks/1")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(200, response.getStatus());
		verify(taskServices).updateTask(1L, taskForm);
	}

	@Test
	void testGetTaskTaskNotFoundError() throws URISyntaxException, Exception {
		ApiResponseDto<TaskDto> apiResponseDto = new ApiResponseDto<>(null, "Task not found.", false,
				HttpStatus.NOT_FOUND.value(), System.currentTimeMillis());
		when(taskServices.getTask(anyLong())).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(
						MockMvcRequestBuilders.get(new URI("/api/v1/tasks/1")).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(404, response.getStatus());
		verify(taskServices).getTask(anyLong());
	}

	@Test
	void testUpdateTaskTaskNotFoundError() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Task not found.", false,
				HttpStatus.NOT_FOUND.value(), System.currentTimeMillis());
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Updated Title 1");
		taskForm.setDescription("Updated Description 1");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.updateTask(1L, taskForm)).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.put(new URI("/api/v1/tasks/1")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(404, response.getStatus());
		verify(taskServices).updateTask(1L, taskForm);
	}

	@Test
	void testAddTaskTitleCannotBeNullError() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Title cannot be null.", false,
				HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
		TaskForm taskForm = new TaskForm();
		taskForm.setDescription("Description 1");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.addTask(taskForm)).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post(new URI("/api/v1/tasks")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(400, response.getStatus());
	}

	@Test
	void testAddTaskTitleCannotBeEmptyError() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Title cannot be empty.", false,
				HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("");
		taskForm.setDescription("Description 1");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.addTask(taskForm)).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post(new URI("/api/v1/tasks")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(400, response.getStatus());
	}

	@Test
	void testAddTaskTitleCannotBeBlankError() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Title cannot be blank.", false,
				HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle(" ");
		taskForm.setDescription("Description 1");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.addTask(taskForm)).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post(new URI("/api/v1/tasks")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(400, response.getStatus());
	}

	@Test
	void testAddTaskDescriptionCannotBeNullError() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Description cannot be null.", false,
				HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.addTask(taskForm)).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post(new URI("/api/v1/tasks")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(400, response.getStatus());
	}

	@Test
	void testAddTaskDescriptionCannotBeEmptyError() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Description cannot be empty.", false,
				HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription("");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.addTask(taskForm)).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post(new URI("/api/v1/tasks")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(400, response.getStatus());
	}

	@Test
	void testAddTaskDescriptionCannotBeBlankError() throws URISyntaxException, Exception {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<>(null, "Description cannot be blank.", false,
				HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription(" ");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.addTask(taskForm)).thenReturn(apiResponseDto);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post(new URI("/api/v1/tasks")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(400, response.getStatus());
	}

	@Test
	void testGetAllTaskDataAccessException() throws URISyntaxException, Exception {
		when(taskServices.getTaskList()).thenThrow(new DataAccessException("Dummy Exception String") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7553480545579482093L;
		});
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get(new URI("/api/v1/tasks")).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(500, response.getStatus());
		verify(taskServices).getTaskList();
	}

	@Test
	void testGetTaskDataAccessException() throws URISyntaxException, Exception {
		when(taskServices.getTask(1L)).thenThrow(new DataAccessException("Dummy Exception String") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7553480545579482093L;
		});
		MvcResult mvcResult = mockMvc
				.perform(
						MockMvcRequestBuilders.get(new URI("/api/v1/tasks/1")).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(500, response.getStatus());
		verify(taskServices).getTask(1L);
	}

	@Test
	void testDeleteTaskDataAccessException() throws URISyntaxException, Exception {
		when(taskServices.deleteTask(1L)).thenThrow(new DataAccessException("Dummy Exception String") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7553480545579482093L;
		});
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.delete(new URI("/api/v1/tasks/1")).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(500, response.getStatus());
		verify(taskServices).deleteTask(1L);
	}

	@Test
	void testAddTaskDataAccessException() throws URISyntaxException, Exception {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription("Description 1");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.addTask(taskForm)).thenThrow(new DataAccessException("Dummy Exception String") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7553480545579482093L;
		});
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.post(new URI("/api/v1/tasks")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(500, response.getStatus());
		verify(taskServices).addTask(taskForm);
	}

	@Test
	void testUpdateTaskDataAccessException() throws URISyntaxException, Exception {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Updated Title 1");
		taskForm.setDescription("Updated Description 1");
		ObjectMapper objectMapper = new ObjectMapper();
		String content = objectMapper.writeValueAsString(taskForm);
		when(taskServices.updateTask(1L, taskForm)).thenThrow(new DataAccessException("Dummy Exception String") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7553480545579482093L;
		});
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.put(new URI("/api/v1/tasks/1")).content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals(500, response.getStatus());
		verify(taskServices).updateTask(1L, taskForm);
	}

}
