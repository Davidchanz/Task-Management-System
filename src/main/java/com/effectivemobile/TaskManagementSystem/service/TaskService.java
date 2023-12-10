package com.effectivemobile.TaskManagementSystem.service;

import com.effectivemobile.TaskManagementSystem.dto.input.task.TaskInputDto;
import com.effectivemobile.TaskManagementSystem.exception.AccessToResourceDeniedException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.TaskNotFoundException;
import com.effectivemobile.TaskManagementSystem.mapper.TaskMapper;
import com.effectivemobile.TaskManagementSystem.model.Comment;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Transactional
@Service
public class TaskService {
    @Autowired
    public TaskRepository taskRepository;

    @Autowired
    public TaskMapper taskMapper;

    public Task findTaskById(long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task with id [" + id + "] not found!"));
    }

    public void addNewTask(Task task) {
        taskRepository.save(task);
    }

    public void updateTask(User user, Task task, TaskInputDto taskInputDto) {
        if(!Objects.equals(task.getAuthor().getId(), user.getId()))
            throw new AccessToResourceDeniedException("You don't have rights to change Task with id [" + task.getId() + "]");

        taskMapper.updateTaskFromDto(taskInputDto, task);
        taskRepository.save(task);
    }

    public Task deleteTask(User user, Long id) {
        Task task = findTaskById(id);
        if(!Objects.equals(task.getAuthor().getId(), user.getId()))
            throw new AccessToResourceDeniedException("You don't have rights to delete Task with id [" + id + "]");

        taskRepository.delete(task);
        return task;
    }

    public Page<Task> findTasksByAuthor(User author, Pageable pageable) {
        return taskRepository.findAllByAuthor(author, pageable);
    }

    public Page<Task> findTasksByExecutor(User executor, Pageable pageable) {
        return taskRepository.findAllByExecutor(executor, pageable);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }
}
