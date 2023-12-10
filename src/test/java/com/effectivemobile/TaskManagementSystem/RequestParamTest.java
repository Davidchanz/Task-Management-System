package com.effectivemobile.TaskManagementSystem;

import com.effectivemobile.TaskManagementSystem.config.SecurityConfig;
import com.effectivemobile.TaskManagementSystem.config.TestConfig;
import com.effectivemobile.TaskManagementSystem.controller.AbstractTest;
import com.effectivemobile.TaskManagementSystem.controller.TaskController;
import com.effectivemobile.TaskManagementSystem.controller.UserController;
import com.effectivemobile.TaskManagementSystem.dto.output.task.TaskDto;
import com.effectivemobile.TaskManagementSystem.exception.AccessToResourceDeniedException;
import com.effectivemobile.TaskManagementSystem.exception.PageIllegalArgumentException;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.PageNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.service.PriorityService;
import com.effectivemobile.TaskManagementSystem.service.StatusService;
import com.effectivemobile.TaskManagementSystem.service.TaskService;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
public class RequestParamTest extends AbstractTest {

    @MockBean
    TaskService taskService;

    @MockBean
    UserService userService;

    @MockBean
    StatusService statusService;

    @MockBean
    PriorityService priorityService;

    @InjectMocks
    TaskController taskController;

    @BeforeEach
    public void setUp() {
        super.setUp(taskController);
        ReflectionTestUtils.setField(taskController, "pageSize", 10);
    }


    @Test
    void PathVariableTypeMismatch_ExceptionThrow() throws Exception {
        this.mvc.perform(delete("/api/task/delete/{id}", "badId")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(result -> assertEquals("Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"badId\"", result.getResolvedException().getMessage()));
    }

    @Test
    void PathVariableMissing_ExceptionThrow() throws Exception {
        this.mvc.perform(delete("/api/task/delete/{id}", " ")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MissingPathVariableException))
                .andExpect(result -> assertEquals("Required URI template variable 'id' for method parameter type Long is present but converted to null", result.getResolvedException().getMessage()));
    }

    @Test
    void RequestBodyParameterMissing_ExceptionThrow() throws Exception {
        this.mvc.perform(post("/api/task/add")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RequiredRequestParamIsMissingException))
                .andExpect(result -> assertEquals("Required request param TaskInputDto is missing", result.getResolvedException().getMessage()));

    }

    @Test
    void RequestBodyParameterNotValid_ExceptionThrow() throws Exception {
        var result = this.mvc.perform(post("/api/task/add")
                        .contentType(APPLICATION_JSON_UTF8).content("{\"par\": \"badreq\"}")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(res -> assertTrue(res.getResolvedException() instanceof MethodArgumentNotValidException))
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString("Status must not be null!"));
        assertThat(result.getResponse().getContentAsString(), containsString("Description text must not be empty!"));
        assertThat(result.getResponse().getContentAsString(), containsString("Title text must not be empty!"));
        assertThat(result.getResponse().getContentAsString(), containsString("Priority must not be null!"));
        assertThat(result.getResponse().getContentAsString(), containsString("Executor must not be null!"));
    }

    @Test
    void RequestParamTypeMismatch_ExceptionThrow() throws Exception {
        var result = this.mvc.perform(get("/api/task/author/{username}?page=badParam", "TestUser")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(res -> assertTrue(res.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(res -> assertEquals("Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; For input string: \"badParam\"", res.getResolvedException().getMessage()));
    }

    @Test
    void PageIllegalArgumentException_ExceptionThrow() throws Exception {
        var result = this.mvc.perform(get("/api/task/author/{username}?page=-1", "TestUser")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isBadRequest())
                .andExpect(res -> assertTrue(res.getResolvedException() instanceof PageIllegalArgumentException))
                .andExpect(res -> assertEquals("Page index must not be less than zero", res.getResolvedException().getMessage()));
    }

    @Test
    void PageNotFoundException_ExceptionThrow() throws Exception {
        Page<Task> tasksPage = Mockito.mock(Page.class);

        when(userService.findUserByUserName(user.getUsername())).thenReturn(user);
        when(taskService.findTasksByAuthor(any(User.class), any(Pageable.class))).thenReturn(tasksPage);
        when(tasksPage.getTotalPages()).thenReturn(1);

        var result = this.mvc.perform(get("/api/task/author/{username}?page=1", "TestUser")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isNotFound())
                .andExpect(res -> assertTrue(res.getResolvedException() instanceof PageNotFoundException))
                .andExpect(res -> assertEquals("Tasks Page number [" + 1 + "] not found!", res.getResolvedException().getMessage()));
    }

}
