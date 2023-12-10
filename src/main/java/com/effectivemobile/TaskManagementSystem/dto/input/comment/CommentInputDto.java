package com.effectivemobile.TaskManagementSystem.dto.input.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CommentInputDto {

    @NotNull(message = "Comment text must not be empty!")
    @Size(min = 10, max = 1024, message = "Comment text size must be between 10 and 1024!")
    private String text;
}
