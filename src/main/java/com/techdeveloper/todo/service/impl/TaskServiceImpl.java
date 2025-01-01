package com.techdeveloper.todo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.techdeveloper.todo.dto.ApiResponseDto;
import com.techdeveloper.todo.dto.TaskDto;
import com.techdeveloper.todo.entity.Task;
import com.techdeveloper.todo.form.TaskForm;
import com.techdeveloper.todo.repository.TaskRepository;
import com.techdeveloper.todo.service.TaskServices;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class TaskServiceImpl implements TaskServices {

	private final TaskRepository taskRepository;

	@Override
	public ApiResponseDto<Void> addTask(TaskForm taskForm) {
		Task task = new Task();
		task.setTitle(taskForm.getTitle());
		task.setDescription(taskForm.getDescription());
		taskRepository.save(task);
		return new ApiResponseDto<>(null, "Task created successfully.", true, HttpStatus.CREATED.value(),
				System.currentTimeMillis());
	}

	@Override
	public ApiResponseDto<Void> updateTask(Long id, TaskForm taskForm) {
		Optional<Task> taskOptional = taskRepository.findById(id);
		if (taskOptional.isPresent()) {
			Task task = taskOptional.get();
			task.setTitle(taskForm.getTitle());
			task.setDescription(taskForm.getDescription());
			taskRepository.save(task);
			return new ApiResponseDto<>(null, "Task updated successfully.", true, HttpStatus.OK.value(),
					System.currentTimeMillis());
		}
		return new ApiResponseDto<>(null, "Task not found.", false, HttpStatus.NOT_FOUND.value(),
				System.currentTimeMillis());
	}

	@Override
	public ApiResponseDto<TaskDto> getTask(Long id) {
		Optional<Task> taskOptional = taskRepository.findById(id);
		if (taskOptional.isPresent()) {
			return new ApiResponseDto<>(new TaskDto(taskOptional.get()), "Task found successfully.", true, HttpStatus.OK.value(),
					System.currentTimeMillis());
		}
		return new ApiResponseDto<>(null, "Task not found.", false, HttpStatus.NOT_FOUND.value(),
				System.currentTimeMillis());
	}

	@Override
	public ApiResponseDto<List<TaskDto>> getTaskList() {
		return new ApiResponseDto<>(taskRepository.findAll().stream().map(TaskDto::new).toList(), "Task list fetched successfully.", true, HttpStatus.OK.value(),
				System.currentTimeMillis());
	}

	@Override
	public ApiResponseDto<Void> deleteTask(Long id) {
		taskRepository.deleteById(id);
		return new ApiResponseDto<>(null, "Task deleted successfully.", true, HttpStatus.OK.value(),
				System.currentTimeMillis());
	}

}
