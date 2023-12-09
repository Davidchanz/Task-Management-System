package com.effectivemobile.TaskManagementSystem.dto;

import com.effectivemobile.TaskManagementSystem.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class StatusDto {
    @NotNull
    private String name;

    public StatusDto(Status status){
        this.setName(status.getName());
    }
}
