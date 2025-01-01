package com.techdeveloper.todo.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techdeveloper.todo.annotation.group.NotBlankGroup;
import com.techdeveloper.todo.annotation.group.NotEmptyGroup;
import com.techdeveloper.todo.annotation.group.NotNullGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskForm {

	@NotNull(message = "Title cannot be null.",groups = NotNullGroup.class)
	@NotEmpty(message = "Title cannot be empty.",groups = NotEmptyGroup.class)
	@NotBlank(message = "Title cannot be blank.",groups = NotBlankGroup.class)
	private String title;
	
	@NotNull(message = "Description cannot be null.",groups = NotNullGroup.class)
	@NotEmpty(message = "Description cannot be empty.",groups = NotEmptyGroup.class)
	@NotBlank(message = "Description cannot be blank.",groups = NotBlankGroup.class)
	private String description;
	
}
