package com.techdeveloper.todo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "task",schema="todoapp")
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1625577019448486746L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="task_id",nullable = false,unique = true)
	private Long id;

	@Column(name="title",nullable = false,unique = true)
	private String title;
	
	@Column(name="description",nullable = false,unique = false)
	private String description;

	@CreationTimestamp
	@Column(name="task_created_at",nullable = false,unique = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name="task_updated_at",nullable = true,unique = false)
	private LocalDateTime updatedAt;
}
