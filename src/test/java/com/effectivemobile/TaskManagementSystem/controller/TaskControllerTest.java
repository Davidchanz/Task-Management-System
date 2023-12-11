package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.StatusDto;
import com.effectivemobile.TaskManagementSystem.dto.input.task.TaskInputDto;
import com.effectivemobile.TaskManagementSystem.dto.output.task.TaskDto;
import com.effectivemobile.TaskManagementSystem.exception.notFound.PageNotFoundException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.TaskNotFoundException;
import com.effectivemobile.TaskManagementSystem.mapper.TaskMapper;
import com.effectivemobile.TaskManagementSystem.mapper.TaskMapperImpl;
import com.effectivemobile.TaskManagementSystem.model.Status;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.repository.TaskRepository;
import com.effectivemobile.TaskManagementSystem.service.PriorityService;
import com.effectivemobile.TaskManagementSystem.service.StatusService;
import com.effectivemobile.TaskManagementSystem.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.effectivemobile.TaskManagementSystem.config.SecurityConfig;
import com.effectivemobile.TaskManagementSystem.config.TestConfig;
import com.effectivemobile.TaskManagementSystem.exception.*;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ TaskController.class })
@Import({ SecurityConfig.class, TestConfig.class })
public class TaskControllerTest extends AbstractTest{
    @MockBean
    TaskService taskService;

    @MockBean
    UserService userService;

    @MockBean
    StatusService statusService;

    @MockBean
    PriorityService priorityService;

    @MockBean
    TaskRepository taskRepository;

    @InjectMocks
    TaskMapper taskMapper = new TaskMapperImpl();

    @InjectMocks
    TaskController taskController;

    TaskInputDto taskInputDto;

    @BeforeEach
    public void setUp() {
        super.setUp(taskController);
        taskService.taskRepository = taskRepository;
        taskService.taskMapper = taskMapper;
        ReflectionTestUtils.setField(taskController, "pageSize", 10);
        taskInputDto = new TaskInputDto();
        taskInputDto.setTitle("Test Task Title");
        taskInputDto.setDescription("TestTaskDescription");
        taskInputDto.setExecutor(anotherUser.getUsername());
        taskInputDto.setStatus(1L);
        taskInputDto.setPriority(1L);
    }

    @Test
    void GetTask_Success() throws Exception {
        doReturn(task).when(taskService).findTaskById(any(Long.class));

        TaskDto taskDto = new TaskDto(task);

        var result = this.mvc.perform(get("/api/task/{id}", 1)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString(task.getTitle()));
    }

    @Test
    void AddNewTask_Success() throws Exception {
        Task newTask = new Task(taskInputDto);
        newTask.setAuthor(user);
        newTask.setExecutor(anotherUser);
        newTask.setStatus(status);
        newTask.setPriority(priority);

        when(principal.getName()).thenReturn(user.getUsername());
        when(userService.findUserByUserName(user.getUsername())).thenReturn(user);
        when(userService.findUserByUserName(anotherUser.getUsername())).thenReturn(anotherUser);
        when(statusService.findStatusById(any(Long.class))).thenReturn(status);
        when(priorityService.findPriorityById(any(Long.class))).thenReturn(priority);
        doCallRealMethod().when(taskService).addNewTask(any(Task.class));
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        String requestJson = ow.writeValueAsString(taskInputDto);

        var result = this.mvc.perform(post("/api/task/add")
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void UpdateTask_Success() throws Exception {
        when(principal.getName()).thenReturn(user.getUsername());
        when(userService.findUserByUserName(user.getUsername())).thenReturn(user);
        when(userService.findUserByUserName(anotherUser.getUsername())).thenReturn(anotherUser);
        when(statusService.findStatusById(any(Long.class))).thenReturn(status);
        when(priorityService.findPriorityById(any(Long.class))).thenReturn(priority);
        when(taskService.findTaskById(any(Long.class))).thenReturn(task);
        doCallRealMethod().when(taskService).updateTask(any(User.class), any(Task.class), any(TaskInputDto.class));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        String requestJson = ow.writeValueAsString(taskInputDto);

        var result = this.mvc.perform(put("/api/task/update/{id}", 1)
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString("Task [" + taskInputDto.getTitle() + "] was updated"));
        assertEquals(taskInputDto.getTitle(), task.getTitle());
    }

    @Test
    void DeleteTask_Success() throws Exception {
        when(principal.getName()).thenReturn(user.getUsername());
        when(userService.findUserByUserName(user.getUsername())).thenReturn(user);
        doCallRealMethod().when(taskService).deleteTask(any(User.class), any(Long.class));
        doCallRealMethod().when(taskService).findTaskById(any(Long.class));
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));

        var result = this.mvc.perform(delete("/api/task/delete/{id}", 1)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString("Task [" + task.getTitle() + "] was deleted!"));
    }

    @Test
    void GetTasksByAuthor_Success() throws Exception {
        Page<Task> tasksPage = Mockito.mock(Page.class);

        when(userService.findUserByUserName(user.getUsername())).thenReturn(user);
        when(taskService.findTasksByAuthor(any(User.class), any(Pageable.class))).thenReturn(tasksPage);
        when(tasksPage.getTotalPages()).thenReturn(2);
        when(tasksPage.getContent()).thenReturn(List.of(task));

        var result = this.mvc.perform(get("/api/task/author/{username}?page=1", "TestUser")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void GetTasksByExecutor_Success() throws Exception {
        Page<Task> tasksPage = Mockito.mock(Page.class);

        when(userService.findUserByUserName(user.getUsername())).thenReturn(user);
        when(taskService.findTasksByExecutor(any(User.class), any(Pageable.class))).thenReturn(tasksPage);
        when(tasksPage.getTotalPages()).thenReturn(2);
        when(tasksPage.getContent()).thenReturn(List.of(task));

        var result = this.mvc.perform(get("/api/task/executor/{username}?page=1", "TestUser")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void UpdateTaskStatusByExecutor_Success() throws Exception {
        StatusDto statusDto = new StatusDto();
        statusDto.setName("TestStatus");
        Status newStatus = new Status();
        newStatus.setName("NewTestStatus");

        when(principal.getName()).thenReturn(anotherUser.getUsername());
        when(userService.findUserByUserName(anotherUser.getUsername())).thenReturn(anotherUser);
        when(taskService.findTaskById(any(Long.class))).thenReturn(task);
        when(statusService.findStatusByName(any(String.class))).thenReturn(newStatus);
        doCallRealMethod().when(taskService).saveTask(any(Task.class));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        String requestJson = ow.writeValueAsString(statusDto);

        var result = this.mvc.perform(put("/api/task/update/status/{id}", 1)
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString("Task [" + task.getTitle() + "] status was updated"));
        assertEquals(newStatus.getName(), task.getStatus().getName());
    }
}

