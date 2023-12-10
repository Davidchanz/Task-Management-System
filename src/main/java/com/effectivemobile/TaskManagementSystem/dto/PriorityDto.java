package com.effectivemobile.TaskManagementSystem.dto;

import com.effectivemobile.TaskManagementSystem.model.Priority;
import com.effectivemobile.TaskManagementSystem.model.Status;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PriorityDto {

    @NotNull(message = "Priority name must not be null!")
    @Size(min = 1, max = 32, message = "Priority name must be between 1 and 32!")
    private String name;

    public PriorityDto(Priority priority){
        this.setName(priority.getName());
    }

    public void setName(String name){
        this.name = name.toUpperCase();
    }
}
