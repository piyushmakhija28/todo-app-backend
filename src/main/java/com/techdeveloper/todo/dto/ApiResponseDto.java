package com.techdeveloper.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@JsonInclude(value = Include.NON_NULL)
public final class ApiResponseDto<T> {

	private final T data;
	
	private final String message;
	
	private final boolean success;
	
	private final int status;
	
	private final long timestamp;
	
}
