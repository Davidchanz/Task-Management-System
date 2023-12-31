package com.effectivemobile.TaskManagementSystem.dto.output.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public abstract class ApiResponse {

    @NotNull
    protected Instant createdOn = Instant.now();

    @NotNull
    protected int status;

    @NotNull
    protected String title;
}
