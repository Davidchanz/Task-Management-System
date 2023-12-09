package com.effectivemobile.TaskManagementSystem.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdOn;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant lastUpdatedOn;
}
