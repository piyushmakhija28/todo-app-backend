package com.techdeveloper.todo.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.techdeveloper.todo.dto.ApiResponseDto;
import com.techdeveloper.todo.dto.TaskDto;
import com.techdeveloper.todo.form.TaskForm;

public interface TaskServices {

	@Transactional(rollbackFor = DataAccessException.class,propagation = Propagation.REQUIRES_NEW)
	public ApiResponseDto<Void> addTask(TaskForm taskForm);

	@Transactional(rollbackFor = DataAccessException.class,propagation = Propagation.REQUIRED)
	public ApiResponseDto<Void> updateTask(Long id, TaskForm taskForm);

	@Transactional(rollbackFor = DataAccessException.class,propagation = Propagation.REQUIRED,readOnly = true)
	public ApiResponseDto<TaskDto> getTask(Long id);

	@Transactional(rollbackFor = DataAccessException.class,propagation = Propagation.REQUIRED,readOnly = true)
	public ApiResponseDto<List<TaskDto>> getTaskList();

	@Transactional(rollbackFor = DataAccessException.class,propagation = Propagation.REQUIRES_NEW)
	public ApiResponseDto<Void> deleteTask(Long id);

}
