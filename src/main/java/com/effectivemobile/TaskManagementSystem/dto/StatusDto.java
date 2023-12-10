package com.effectivemobile.TaskManagementSystem.dto;

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
public class StatusDto {

    @NotNull(message = "Status name must not be null!")
    @Size(min = 1, max = 32, message = "Status name must be between 1 and 32!")
    private String name;

    public StatusDto(Status status){
        this.setName(status.getName());
    }

    public void setName(String name){
        this.name = name.toUpperCase();
    }
}
