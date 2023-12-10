package com.effectivemobile.TaskManagementSystem.service;

import com.effectivemobile.TaskManagementSystem.dto.input.task.TaskInputDto;
import com.effectivemobile.TaskManagementSystem.exception.AccessToResourceDeniedException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.TaskNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("TEST")
public class TaskServiceTest {

    @MockBean
    TaskService taskService;

    @MockBean
    TaskRepository taskRepository;

    @BeforeEach
    void setUp(){
        taskService.taskRepository = taskRepository;
    }

    @Test
    void TaskWithIdNotFound_ExceptionThrow(){
        doCallRealMethod().when(taskService).findTaskById(any(Long.class));
        doReturn(Optional.empty()).when(taskRepository).findById(any(Long.class));

        Exception exception = assertThrows(TaskNotFoundException.class, () -> taskService.findTaskById(1L));
        String expectedMessage = "Task with id [" + 1 + "] not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void AccessToDeleteTaskDenied_ExceptionThrow() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Task task = new Task();
        task.setId(1L);
        task.setAuthor(user2);

        doCallRealMethod().when(taskService).deleteTask(any(User.class), any(Long.class));
        doReturn(task).when(taskService).findTaskById(any(Long.class));

        Exception exception = assertThrows(AccessToResourceDeniedException.class, () -> taskService.deleteTask(user1, 1L));
        String expectedMessage = "You don't have rights to delete Task with id [" + task.getId() + "]";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void AccessToUpdateTaskDenied_ExceptionThrow() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        Task task = new Task();
        task.setId(1L);
        task.setAuthor(user2);

        doCallRealMethod().when(taskService).updateTask(any(User.class), any(Task.class), any(TaskInputDto.class));

        Exception exception = assertThrows(AccessToResourceDeniedException.class, () -> taskService.updateTask(user1, task, new TaskInputDto()));
        String expectedMessage = "You don't have rights to update Task with id [" + 1 + "]";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
