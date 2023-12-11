package com.effectivemobile.TaskManagementSystem.dto.input.auth;

import com.effectivemobile.TaskManagementSystem.validation.annotation.ValidEmail;
import com.effectivemobile.TaskManagementSystem.validation.annotation.ValidPassword;
import com.effectivemobile.TaskManagementSystem.validation.annotation.ValidUserName;
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
    @ValidUserName
    private String username;

    @NotNull(message = "Password must not be null!")
    @ValidPassword
    private String password;

}
