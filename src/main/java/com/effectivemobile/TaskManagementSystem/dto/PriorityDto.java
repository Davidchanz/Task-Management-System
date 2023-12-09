package com.effectivemobile.TaskManagementSystem.dto;

import com.effectivemobile.TaskManagementSystem.model.Priority;
import com.effectivemobile.TaskManagementSystem.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PriorityDto {
    @NotNull
    private String name;

    public PriorityDto(Priority priority){
        this.setName(priority.getName());
    }
}
