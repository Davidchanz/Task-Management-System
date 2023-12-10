package com.effectivemobile.TaskManagementSystem.model;


import com.effectivemobile.TaskManagementSystem.dto.input.comment.CommentInputDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COMMENTS")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 1024)
    @NotNull
    private String text;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User author;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Task task;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant lastUpdatedOn;

    public Comment(CommentInputDto commentInputDto){
        this.setText(commentInputDto.getText());
    }
}
