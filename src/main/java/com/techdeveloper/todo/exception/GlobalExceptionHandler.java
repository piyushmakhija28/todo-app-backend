package com.techdeveloper.todo.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.techdeveloper.todo.dto.ApiResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ApiResponseDto<Void>> handleDataAccessException(DataAccessException dataAccessException) {
		ApiResponseDto<Void> apiResponseDto = new ApiResponseDto<Void>(null, dataAccessException.getMessage(), false,
				HttpStatus.INTERNAL_SERVER_ERROR.value(), System.currentTimeMillis());
		return new ResponseEntity<ApiResponseDto<Void>>(apiResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
