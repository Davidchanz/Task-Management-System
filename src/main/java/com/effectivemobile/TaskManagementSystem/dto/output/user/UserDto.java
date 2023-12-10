package com.effectivemobile.TaskManagementSystem.dto.output.user;

import com.effectivemobile.TaskManagementSystem.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Setter
@Getter
@ToString
public class UserDto {
    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private Instant createdOn;

    public UserDto(User user){
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdOn = user.getCreatedOn();
    }
}
