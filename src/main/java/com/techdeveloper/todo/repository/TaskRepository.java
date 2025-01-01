package com.techdeveloper.todo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techdeveloper.todo.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Serializable>{

}
