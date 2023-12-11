package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.config.SecurityConfig;
import com.effectivemobile.TaskManagementSystem.config.TestConfig;
import com.effectivemobile.TaskManagementSystem.dto.StatusDto;
import com.effectivemobile.TaskManagementSystem.dto.input.comment.CommentInputDto;
import com.effectivemobile.TaskManagementSystem.dto.output.comment.CommentDto;
import com.effectivemobile.TaskManagementSystem.model.Comment;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ CommentController.class })
@Import({ SecurityConfig.class, TestConfig.class })
class CommentControllerTest extends AbstractTest{

    @MockBean
    TaskService taskService;

    @MockBean
    UserService userService;

    @MockBean
    CommentService commentService;

    @InjectMocks
    CommentController commentController;

    @BeforeEach
    void setUp() {
        super.setUp(commentController);
        ReflectionTestUtils.setField(commentController, "pageSize", 10);
    }

    @Test
    void GetComment_Success() throws Exception {
        doReturn(comment).when(commentService).findCommentById(any(Long.class));

        CommentDto commentDto = new CommentDto(comment);

        var result = this.mvc.perform(get("/api/comment/{id}", 1)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString(commentDto.getText()));
    }

    @Test
    void GetCommentsByTask_Success() throws Exception {
        Page<Comment> commentsPage = Mockito.mock(Page.class);

        doReturn(comment).when(commentService).findCommentById(any(Long.class));
        when(taskService.findTaskById(any(Long.class))).thenReturn(task);
        when(commentService.findCommentsByTask(any(Task.class), any(Pageable.class))).thenReturn(commentsPage);
        when(commentsPage.getTotalPages()).thenReturn(2);
        when(commentsPage.getContent()).thenReturn(List.of(comment));

        var result = this.mvc.perform(get("/api/comment/{id}?page=1", 1)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void AddCommentToTask_Success() throws Exception  {
        when(principal.getName()).thenReturn(user.getUsername());
        when(userService.findUserByUserName(user.getUsername())).thenReturn(user);
        doReturn(task).when(taskService).findTaskById(any(Long.class));
        doNothing().when(commentService).addNewComment(any(Comment.class));

        CommentInputDto commentInputDto = new CommentInputDto();
        commentInputDto.setText("TestComment");

        String requestJson = ow.writeValueAsString(commentInputDto);

        var result = this.mvc.perform(post("/api/comment/add/{id}", 1)
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString(commentInputDto.getText()));
    }
}