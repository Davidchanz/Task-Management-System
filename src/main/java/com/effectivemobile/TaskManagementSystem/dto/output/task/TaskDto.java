package com.effectivemobile.TaskManagementSystem.dto.output.task;

import com.effectivemobile.TaskManagementSystem.dto.PriorityDto;
import com.effectivemobile.TaskManagementSystem.dto.StatusDto;
import com.effectivemobile.TaskManagementSystem.dto.output.user.UserDto;
import com.effectivemobile.TaskManagementSystem.model.Task;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Setter
@Getter
@ToString
public class TaskDto {
    @NotNull
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private UserDto author;

    @NotNull
    private UserDto executor;

    @NotNull
    private StatusDto status;

    @NotNull
    private PriorityDto priority;

    @NotNull
    private Instant createdOn;

    @NotNull
    private Instant lastUpdatedOn;

    public TaskDto(Task task){
        this.setId(task.getId());
        this.setDescription(task.getDescription());
        this.setTitle(task.getTitle());
        this.setStatus(new StatusDto(task.getStatus()));
        this.setPriority(new PriorityDto(task.getPriority()));
        this.setAuthor(new UserDto(task.getAuthor()));
        this.setExecutor(new UserDto(task.getExecutor()));
        this.setCreatedOn(task.getCreatedOn());
        this.setLastUpdatedOn(task.getLastUpdatedOn());
    }
}
