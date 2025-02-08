package com.techdeveloper.todo.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import com.techdeveloper.todo.dto.ApiResponseDto;
import com.techdeveloper.todo.dto.TaskDto;
import com.techdeveloper.todo.entity.Task;
import com.techdeveloper.todo.form.TaskForm;
import com.techdeveloper.todo.repository.TaskRepository;
import com.techdeveloper.todo.service.TaskServices;

class TaskServiceImplTest {

	private TaskRepository taskRepository;

	private TaskServices taskServices;

	@BeforeEach
	void init() {
		taskRepository = mock(TaskRepository.class);
		taskServices = new TaskServiceImpl(taskRepository);
	}

	@Test
	void getTaskListSuccessTest() {
		List<Task> taskList = new ArrayList<>();
		when(taskRepository.findAll()).thenReturn(taskList);
		ApiResponseDto<List<TaskDto>> apiResponseDto = taskServices.getTaskList();
		assertTrue(apiResponseDto.isSuccess());
		verify(taskRepository).findAll();
	}

	@Test
	void getTaskListDataAccessExceptionTest() {
		doThrow(new DataAccessException("Unable to connect to db.") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		}).when(taskRepository).findAll();
		assertThrows(DataAccessException.class, () -> taskServices.getTaskList());
		verify(taskRepository).findAll();
	}

	@Test
	void getTaskByIdSuccessTest() {
		Task task = new Task();
		task.setId(1L);
		task.setTitle("Title 1");
		task.setDescription("Description 1");
		task.setCreatedAt(LocalDateTime.now());
		Optional<Task> optionalTask = Optional.ofNullable(task);
		when(taskRepository.findById(1L)).thenReturn(optionalTask);
		ApiResponseDto<TaskDto> apiResponseDto = taskServices.getTask(1L);
		assertTrue(apiResponseDto.isSuccess());
		verify(taskRepository).findById(1L);
	}

	@Test
	void getTaskByIdDataAccessExceptionTest() {
		doThrow(new DataAccessException("Unable to connect to db.") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4713859052997359109L;
		}).when(taskRepository).findById(1L);
		assertThrows(DataAccessException.class, () -> taskServices.getTask(1L));
		verify(taskRepository).findById(1L);
	}

	@Test
	void getTaskByIdTaskNotFoundTest() {
		Optional<Task> optionalTask = Optional.empty();
		when(taskRepository.findById(1L)).thenReturn(optionalTask);
		ApiResponseDto<TaskDto> apiResponseDto = taskServices.getTask(1L);
		assertFalse(apiResponseDto.isSuccess());
		verify(taskRepository).findById(1L);
	}

	@Test
	void deleteTaskSuccessTest() {
		doNothing().when(taskRepository).deleteById(1L);
		ApiResponseDto<Void> apiResponseDto = taskServices.deleteTask(1L);
		assertTrue(apiResponseDto.isSuccess());
		verify(taskRepository).deleteById(1L);
	}

	@Test
	void deleteTaskDataAccessExceptionTest() {
		doThrow(new DataAccessException("Unable to connect to db.") {

			/**
			 * 
			 */
			private static final long serialVersionUID = -668162040387156993L;
		}).when(taskRepository).deleteById(1L);
		assertThrows(DataAccessException.class, () -> taskServices.deleteTask(1L));
		verify(taskRepository).deleteById(1L);
	}

	@Test
	void addTaskSuccessTest() {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription("Description 1");
		Task task = new Task();
		task.setTitle(taskForm.getTitle());
		task.setDescription(taskForm.getDescription());
		Task task2 = task;
		task2.setId(1L);
		task2.setCreatedAt(LocalDateTime.now());
		when(taskRepository.save(task)).thenReturn(task2);
		ApiResponseDto<Void> apiResponseDto = taskServices.addTask(taskForm);
		assertTrue(apiResponseDto.isSuccess());
		task.setId(null);
		task.setCreatedAt(null);
		verify(taskRepository).save(task);
	}

	@Test
	void addTaskDataAccessExceptionTest() {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription("Description 1");
		Task task = new Task();
		task.setTitle(taskForm.getTitle());
		task.setDescription(taskForm.getDescription());
		doThrow(new DataAccessException("Unable to connect db.") {

			/**
			 * 
			 */
			private static final long serialVersionUID = -6588993127044311877L;
		}).when(taskRepository).save(task);
		assertThrows(DataAccessException.class, () -> taskServices.addTask(taskForm));
		verify(taskRepository).save(task);
	}

	@Test
	void updateTaskSuccessTest() {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription("Description 1");
		Task task = new Task();
		task.setId(1L);
		task.setCreatedAt(LocalDateTime.now());
		task.setTitle(taskForm.getTitle());
		task.setDescription(taskForm.getDescription());
		when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
		task.setTitle("Updated Title");
		task.setDescription("Updated Description");
		when(taskRepository.save(task)).thenReturn(task);
		ApiResponseDto<Void> apiResponseDto = taskServices.updateTask(1L, taskForm);
		assertTrue(apiResponseDto.isSuccess());
		verify(taskRepository).findById(1L);
		task.setTitle(taskForm.getTitle());
		task.setDescription(taskForm.getDescription());
		verify(taskRepository).save(task);
	}
	
	@Test
	void updateTaskDataAccessExceptionForFindByIdTest() {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription("Description 1");
		doThrow(new DataAccessException("Unable to connect to db.") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7476788707123539119L;
		}).when(taskRepository).findById(1L);
		assertThrows(DataAccessException.class, ()->taskServices.updateTask(1L, taskForm));
		verify(taskRepository).findById(1L);
	}
	
	@Test
	void updateTaskDataAccessExceptionForSaveTest() {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription("Description 1");
		Task task = new Task();
		task.setId(1L);
		task.setCreatedAt(LocalDateTime.now());
		task.setTitle(taskForm.getTitle());
		task.setDescription(taskForm.getDescription());
		when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
		task.setTitle("Updated Title");
		task.setDescription("Updated Description");
		doThrow(new DataAccessException("Unable to connect to db.") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 7476788707123539119L;
		}).when(taskRepository).save(task);
		assertThrows(DataAccessException.class, ()->taskServices.updateTask(1L, taskForm));
		verify(taskRepository).findById(1L);
		task.setTitle(taskForm.getTitle());
		task.setDescription(taskForm.getDescription());
		verify(taskRepository).save(task);
	}
	
	@Test
	void updateTaskTaskNotFoundTest() {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title 1");
		taskForm.setDescription("Description 1");
		when(taskRepository.findById(1L)).thenReturn(Optional.empty());
		ApiResponseDto<Void> apiResponseDto = taskServices.updateTask(1L, taskForm);
		assertFalse(apiResponseDto.isSuccess());
		verify(taskRepository).findById(1L);
	}
}
