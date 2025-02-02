package com.techdeveloper.todo.dto;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.techdeveloper.todo.entity.Task;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonInclude(value = Include.NON_NULL)
@NoArgsConstructor
public class TaskDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -205334937967117487L;

	private Long id;

	private String title;

	private String description;

	private String createdAt;

	private String updatedAt;

	public TaskDto(Task task) {
		this.id = task.getId();
		this.title = task.getTitle();
		this.description = task.getDescription();
		this.createdAt = task.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		if (Objects.nonNull(task.getUpdatedAt())) {
			this.updatedAt = task.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		}
	}

}
