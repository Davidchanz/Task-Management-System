package com.effectivemobile.TaskManagementSystem.dto.output.comment;

import com.effectivemobile.TaskManagementSystem.dto.output.user.UserDto;
import com.effectivemobile.TaskManagementSystem.model.Comment;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentDto {
    @NotNull
    private Long id;

    @NotNull
    private String text;

    @NotNull
    private UserDto author;

    @NotNull
    private Instant createdOn;

    @NotNull
    private Instant lastUpdatedOn;
    public CommentDto(Comment comment){
        this.setId(comment.getId());
        this.setText(comment.getText());
        this.setAuthor(new UserDto(comment.getAuthor()));
        this.setCreatedOn(comment.getCreatedOn());
        this.setLastUpdatedOn(comment.getLastUpdatedOn());

    }
}
