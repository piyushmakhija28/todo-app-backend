package com.techdeveloper.todo.service;

import java.util.List;

import com.techdeveloper.todo.dto.ApiResponseDto;
import com.techdeveloper.todo.dto.TaskDto;
import com.techdeveloper.todo.form.TaskForm;

public interface TaskServices {

	public ApiResponseDto<Void> addTask(TaskForm taskForm);

	public ApiResponseDto<Void> updateTask(Long id, TaskForm taskForm);

	public ApiResponseDto<TaskDto> getTask(Long id);

	public ApiResponseDto<List<TaskDto>> getTaskList();

	public ApiResponseDto<Void> deleteTask(Long id);

}
