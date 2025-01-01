package com.techdeveloper.todo.annotation.sequence;

import com.techdeveloper.todo.annotation.group.NotBlankGroup;
import com.techdeveloper.todo.annotation.group.NotEmptyGroup;
import com.techdeveloper.todo.annotation.group.NotNullGroup;

import jakarta.validation.GroupSequence;

@GroupSequence(value = {
		NotNullGroup.class,
		NotEmptyGroup.class,
		NotBlankGroup.class
})
public interface ValidationSequence {

}
