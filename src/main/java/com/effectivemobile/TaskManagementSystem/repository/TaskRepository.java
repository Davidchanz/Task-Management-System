package com.effectivemobile.TaskManagementSystem.repository;

import com.effectivemobile.TaskManagementSystem.model.Comment;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAuthor(User author);

    List<Task> findAllByExecutor(User executor);

    @EntityGraph(attributePaths = "comments")
    Optional<Task> findTaskById(Long id);
}
