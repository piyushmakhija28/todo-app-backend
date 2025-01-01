package com.techdeveloper.todo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techdeveloper.todo.annotation.sequence.ValidationSequence;
import com.techdeveloper.todo.dto.ApiResponseDto;
import com.techdeveloper.todo.dto.TaskDto;
import com.techdeveloper.todo.form.TaskForm;
import com.techdeveloper.todo.service.TaskServices;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/tasks")
@AllArgsConstructor
@CrossOrigin(origins = {
		"http://localhost:4200"
})
public class TaskRestController {

	private final TaskServices taskServices;

	@PostMapping
	public ResponseEntity<ApiResponseDto<Void>> addTask(@Validated(value = ValidationSequence.class) @RequestBody TaskForm taskForm,BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
			ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<Void>(null, message, false, HttpStatus.BAD_REQUEST.value(), System.currentTimeMillis());
			return new ResponseEntity<>(apiResponseDto, HttpStatus.valueOf(apiResponseDto.getStatus()));
		}
		ApiResponseDto<Void> apiResponseDto = taskServices.addTask(taskForm);
		return new ResponseEntity<>(apiResponseDto, HttpStatus.valueOf(apiResponseDto.getStatus()));
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ApiResponseDto<Void>> updateTask(@PathVariable Long id, @RequestBody TaskForm taskForm) {
		ApiResponseDto<Void> apiResponseDto = taskServices.updateTask(id, taskForm);
		return new ResponseEntity<>(apiResponseDto, HttpStatus.valueOf(apiResponseDto.getStatus()));
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ApiResponseDto<TaskDto>> getTask(@PathVariable Long id) {
		ApiResponseDto<TaskDto> apiResponseDto = taskServices.getTask(id);
		return new ResponseEntity<>(apiResponseDto, HttpStatus.valueOf(apiResponseDto.getStatus()));
	}

	@GetMapping
	public ResponseEntity<ApiResponseDto<List<TaskDto>>> getTaskList() {
		ApiResponseDto<List<TaskDto>> apiResponseDto = taskServices.getTaskList();
		return new ResponseEntity<>(apiResponseDto, HttpStatus.valueOf(apiResponseDto.getStatus()));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ApiResponseDto<Void>> deleteTask(@PathVariable Long id) {
		ApiResponseDto<Void> apiResponseDto = taskServices.deleteTask(id);
		return new ResponseEntity<>(apiResponseDto, HttpStatus.valueOf(apiResponseDto.getStatus()));
	}
}
