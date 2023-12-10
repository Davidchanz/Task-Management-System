package com.effectivemobile.TaskManagementSystem.dto.output.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {
    @NotNull
    private String token;
}
