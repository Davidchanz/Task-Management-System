package com.effectivemobile.TaskManagementSystem.model;

import com.effectivemobile.TaskManagementSystem.dto.TaskUpdateDto;
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
@Table(name = "TASKS")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 128)
    @NotNull
    private String title;

    @Column(nullable = false, length = 1024)
    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    private User executor;

    @ManyToOne(fetch = FetchType.EAGER)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    private Priority priority;

    @ToString.Exclude
    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "TASK_COMMENTS",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private Set<Comment> comments = new HashSet<>();

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant lastUpdatedOn;

    public Task(TaskUpdateDto taskUpdateDto) {
        this.setDescription(taskUpdateDto.getDescription());
        this.setTitle(taskUpdateDto.getTitle());
    }
}
