package com.effectivemobile.TaskManagementSystem.dto.output.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseSingleOk extends ApiResponse{

    public ApiResponseSingleOk(String title, String response) {
        this.response = response;
        this.status = 200;
        this.title = title;
    }

    @NotNull
    @Size(max = 1000)
    private String response;
}
