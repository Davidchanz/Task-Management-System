package com.effectivemobile.TaskManagementSystem.dto.input.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class TaskInputDto {

    @NotNull(message = "Title text must not be empty!")
    @Size(min = 10, max = 128, message = "Title size must be between 10 and 128!")
    private String title;

    @NotNull(message = "Description text must not be empty!")
    @Size(max = 128, message = "Description size must be less than 1024!")
    private String description;

    @NotNull(message = "Executor must not be null!")
    private String executor;

    @NotNull(message = "Status must not be null!")
    private Long status;

    @NotNull(message = "Priority must not be null!")
    private Long priority;
}
