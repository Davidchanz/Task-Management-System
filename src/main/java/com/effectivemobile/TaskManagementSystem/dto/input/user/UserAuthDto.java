package com.effectivemobile.TaskManagementSystem.dto.input.user;

import com.effectivemobile.TaskManagementSystem.validation.annotation.PasswordMatches;
import com.effectivemobile.TaskManagementSystem.validation.annotation.ValidEmail;
import com.effectivemobile.TaskManagementSystem.validation.annotation.ValidPassword;
import com.effectivemobile.TaskManagementSystem.validation.annotation.ValidUserName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@PasswordMatches
public class UserAuthDto {
    @NotNull(message = "Username must not be null!")
    @ValidUserName
    private String username;

    @NotNull(message = "Password must not be null!")
    @ValidPassword
    private String password;

    @NotNull(message = "Confirm Password must not be null!")
    private String matchingPassword;

    @ValidEmail
    @NotNull(message = "Email must not be null!")
    private String email;
}
