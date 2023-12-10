package com.effectivemobile.TaskManagementSystem.dto.input.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginDto {
    @NotNull(message = "Username must not be null!")
    @Size(min = 5, max = 25, message = "Username must be between 5 and 25!")
    private String username;

    @NotNull(message = "Password must not be null!")
    @Size(min = 8, max = 25, message = "Password must be between 8 and 25!")
    private String password;

}
